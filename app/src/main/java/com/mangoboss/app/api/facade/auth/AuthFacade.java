package com.mangoboss.app.api.facade.auth;

import com.mangoboss.app.domain.service.auth.AuthService;
import com.mangoboss.app.dto.auth.response.JwtResponse;
import org.springframework.stereotype.Service;

import com.mangoboss.app.dto.KakaoUserInfo;
import com.mangoboss.app.dto.auth.requeset.RefreshTokenRequest;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.auth.requeset.LoginRequest;
import com.mangoboss.app.common.util.JwtUtil;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthFacade {
	private final UserService userService;
	private final AuthService authService;

	public JwtResponse reissueAccessToken(final RefreshTokenRequest refreshTokenRequest) {
		final String refreshToken = refreshTokenRequest.refreshToken();
		final Long userId = authService.validateAndExtractIdFromRefreshToken(refreshToken);
		final UserEntity user = userService.getByUserId(userId);
		return authService.generateToken(user);
	}

	public JwtResponse socialLogin(final LoginRequest loginRequest) {
		final KakaoUserInfo kakaoUserInfo = authService.socialLogin(loginRequest);
		final UserEntity user = userService.createUserByKakao(kakaoUserInfo);
		return authService.generateToken(user);
	}
}
