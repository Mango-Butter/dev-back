package com.mangoboss.app.api.facade;

import javax.security.auth.login.LoginException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.security.OAuth;
import com.mangoboss.app.dto.KakaoUserInfo;
import com.mangoboss.app.dto.ReissueAccessTokenRequest;
import com.mangoboss.app.dto.TokenReissueResponse;
import com.mangoboss.app.domain.service.UserService;
import com.mangoboss.app.dto.LoginRequest;
import com.mangoboss.app.dto.LoginResponse;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.util.JwtUtil;
import com.mangoboss.storage.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthFacade {
	private final JwtUtil jwtUtil;
	private final OAuth oAuth;
	private final UserService userService;

	@Transactional
	public TokenReissueResponse reissueAccessToken(final ReissueAccessTokenRequest reissueAccessTokenRequest) {
		String refreshToken = reissueAccessTokenRequest.refreshToken();
		if (!jwtUtil.validateToken(refreshToken)) {
			throw new CustomException(CustomErrorInfo.UNAUTHORIZED);
		}

		Long kakaoId = jwtUtil.getKakaoId(refreshToken);
		UserEntity userEntity = userService.getByKakaoIdOrThrow(kakaoId);

		String accessToken = jwtUtil.createAccessToken(userEntity.getKakaoId(), userEntity.getRole().toString());

		return TokenReissueResponse.create(accessToken, refreshToken);
	}

	public LoginResponse socialLogin(final LoginRequest loginRequest) {
		try {
			String kakaoAccessToken = oAuth.requestKakaoAccessToken(loginRequest);
			KakaoUserInfo kakaoUserInfo = oAuth.getUserInfoFromKakao(kakaoAccessToken);

			UserEntity userEntity = userService.getOrCreateUser(kakaoUserInfo);
			return createJwtTokens(userEntity);

		} catch (LoginException e) {
			throw new CustomException(CustomErrorInfo.UNAUTHORIZED);
		}
	}

	private LoginResponse createJwtTokens(final UserEntity userEntity) {
		String accessToken = jwtUtil.createAccessToken(userEntity.getKakaoId(), userEntity.getRole().toString());
		String refreshToken = jwtUtil.createRefreshToken(userEntity.getKakaoId(), userEntity.getRole().toString());
		return LoginResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
