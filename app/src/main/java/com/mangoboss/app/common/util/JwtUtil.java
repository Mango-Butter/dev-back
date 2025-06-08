package com.mangoboss.app.common.util;

import com.mangoboss.storage.user.Role;
import java.util.Date;

import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "X-Refresh-Token";

    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_ROLE = "role";

    private final SecretKey accessSecret;
    private final SecretKey refreshSecret;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTime;
    private final String issuer;

    public JwtUtil(
            @Value("${spring.jwt.secret.access-secret-key}") final String accessSecretKey,
            @Value("${spring.jwt.secret.refresh-secret-key}") final String refreshSecretKey,
            @Value("${spring.jwt.token.access-expiration-time}") final Long accessExpirationTime,
            @Value("${spring.jwt.token.refresh-expiration-time}") final Long refreshExpirationTime,
            @Value("${spring.jwt.token.issuer}") final String issuer
    ) {
        this.accessSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretKey));
        this.refreshSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecretKey));
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
        this.issuer = issuer;
    }

    public String generateAccessToken(final Long userId, final Role role) {
        return generateToken(userId, role, accessSecret, accessExpirationTime);
    }

    public String generateRefreshToken(final Long userId, final Role role) {
        return generateToken(userId, role, refreshSecret, refreshExpirationTime);
    }

    private String generateToken(final Long userId, final Role role, final SecretKey secret, final Long expirationTime) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(userId));
        claims.put(CLAIM_USER_ID, userId);
        claims.put(CLAIM_ROLE, role.toString());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secret, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT 검증
    public boolean validateToken(final String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(accessSecret).build().parseClaimsJws(token).getBody();
            // 필수 claim 검사
            return claims.getExpiration().after(new Date()) && // 만료되지 않음
                    claims.getIssuer().equals(issuer); // 시스템에서 발급
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token: {}", e.getMessage());
            throw new CustomException(CustomErrorInfo.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new CustomException(CustomErrorInfo.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new CustomException(CustomErrorInfo.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new CustomException(CustomErrorInfo.ILLEGAL_ARGUMENT_TOKEN);
        }
    }

    // JWT Claims 추출
    public Claims parseClaims(final String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(accessSecret)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public long getRefreshTokenRemainingExpiration(final String refreshToken) {
        Claims claims = parseClaims(refreshToken, false);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }

    private Claims parseClaims(final String token, final boolean isAccessToken) {
        try {
            SecretKey secret = isAccessToken ? accessSecret : refreshSecret;
            return Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();  // 만료된 토큰일 경우에도 claims 추출 가능
        }
    }
}
