package com.mangoboss.app.common.util;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.storage.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final Key key;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    @Value("${spring.jwt.token.issuer}")
    private String issuer;


    @Autowired
    public JwtUtil(@Value("${spring.jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(final UserEntity user) {
        return createToken(user, accessExpirationTime);
    }

    public String createRefreshToken(final UserEntity user) {
        return createToken(user, refreshExpirationTime);
    }

    private String createToken(final UserEntity user, final long expirationTime) {
        Claims claims = Jwts.claims().setSubject(String.valueOf(user.getKakaoId()));
        claims.put("kakaoId", user.getKakaoId());
        claims.put("role", user.getRole().toString());

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.getKakaoId().toString())
            .setIssuer(issuer)
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Long getKakaoId(final String token) {
        return parseClaims(token).get("kakaoId", Long.class);
    }

    public String getRole(final String token) {
        return parseClaims(token).get("role", String.class);
    }

    // JWT 검증
    public boolean validateToken(final String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
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
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
