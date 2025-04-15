package com.mangoboss.app.api.facade;

import org.springframework.stereotype.Service;

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

	public TokenReissueResponse reissueAccessToken(final ReissueAccessTokenRequest reissueAccessTokenRequest) {
		String refreshToken = reissueAccessTokenRequest.refreshToken();
		if (!jwtUtil.validateToken(refreshToken)) {
			throw new CustomException(CustomErrorInfo.UNAUTHORIZED);
		}

		Long userId = jwtUtil.getUserId(refreshToken);
		UserEntity userEntity = userService.getByUserId(userId);

		String accessToken = jwtUtil.createAccessToken(userEntity);

		return TokenReissueResponse.create(accessToken, refreshToken);
	}

	public LoginResponse socialLogin(final LoginRequest loginRequest) {
		String kakaoAccessToken = oAuth.requestKakaoAccessToken(loginRequest);
		KakaoUserInfo kakaoUserInfo = oAuth.getUserInfoFromKakao(kakaoAccessToken);
		kakaoUserInfo.validate(); // 👈 여기서 유효성 검사
		return userService.generateJwtForKakaoUser(kakaoUserInfo);
	}
}
