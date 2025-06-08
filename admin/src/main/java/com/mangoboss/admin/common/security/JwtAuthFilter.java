package com.mangoboss.admin.common.security;

import com.mangoboss.admin.common.exception.CustomException;
import com.mangoboss.admin.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.mangoboss.admin.common.util.JwtUtil.AUTHORIZATION_HEADER;
import static com.mangoboss.admin.common.util.JwtUtil.BEARER_PREFIX;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authorization = request.getHeader(AUTHORIZATION_HEADER);

            if (authorization != null) {
                final String accessToken = extractAccessTokenFromAuthorization(authorization);

                // JWT 유효성 검증
                if (jwtUtil.validateToken(accessToken)) {
                    Claims claims = jwtUtil.parseClaims(accessToken);
                    UserDetails userDetails = new CustomUserDetails(claims);
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        } catch (CustomException ex) {
            SecurityContextHolder.clearContext();
            request.setAttribute("exception", ex);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        } catch (Exception ex) {
            throw new ServletException("Authentication error", ex);
        }
    }

    private String extractAccessTokenFromAuthorization(final String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(7);
        }
        return null;
    }
}
