package com.mangoboss.app.common.security;

import com.mangoboss.app.common.util.JwtUtil;
import io.jsonwebtoken.Claims;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    @Getter
    private final Long userId;
    private final String role;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Claims claims) {
        Number id = (Number) claims.get(JwtUtil.CLAIM_USER_ID);
        this.userId = id.longValue();
        this.role = (String) claims.get(JwtUtil.CLAIM_ROLE);
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
