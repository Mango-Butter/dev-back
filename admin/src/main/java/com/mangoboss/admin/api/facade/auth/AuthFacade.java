package com.mangoboss.admin.api.facade.auth;


import com.mangoboss.admin.domain.service.AuthService;
import com.mangoboss.admin.domain.service.UserService;
import com.mangoboss.admin.dto.auth.requeset.LoginRequest;
import com.mangoboss.admin.dto.auth.requeset.RefreshTokenRequest;
import com.mangoboss.admin.dto.auth.response.JwtResponse;
import com.mangoboss.admin.dto.user.KakaoUserInfo;
import com.mangoboss.storage.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthFacade {
	private final UserService userService;
	private final AuthService authService;

	public JwtResponse reissueAccessToken(final RefreshTokenRequest refreshTokenRequest) {
		final String refreshToken = refreshTokenRequest.refreshToken();
		final Long userId = authService.validateAndExtractIdFromRefreshToken(refreshToken);
		final UserEntity user = userService.getUserById(userId);
		return authService.generateToken(user);
	}

	public JwtResponse socialLogin(final LoginRequest loginRequest) {
		final KakaoUserInfo kakaoUserInfo = authService.socialLogin(loginRequest);
		final UserEntity user = userService.getOrCreateUser(kakaoUserInfo);
		return authService.generateToken(user);
	}
}
