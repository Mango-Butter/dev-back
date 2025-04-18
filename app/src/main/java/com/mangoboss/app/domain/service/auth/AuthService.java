package com.mangoboss.app.domain.service.auth;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.security.KakaoSocialLogin;
import com.mangoboss.app.common.util.JwtUtil;
import com.mangoboss.app.dto.KakaoUserInfo;
import com.mangoboss.app.dto.user.requeset.LoginRequest;
import com.mangoboss.app.dto.user.response.JwtResponse;
import com.mangoboss.storage.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtUtil jwtUtil;
    private final KakaoSocialLogin kakaoSocialLogin;

    public KakaoUserInfo socialLogin(final LoginRequest loginRequest) {
        final String kakaoAccessToken = kakaoSocialLogin.requestKakaoAccessToken(loginRequest);
        final KakaoUserInfo kakaoUserInfo = kakaoSocialLogin.getUserInfoFromKakao(kakaoAccessToken);
        kakaoUserInfo.validate(); // 👈 여기서 유효성 검사
        return kakaoUserInfo;
    }

    public void validateRefreshToken(final String refreshToken){
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(CustomErrorInfo.UNAUTHORIZED);
        }
    }

    public JwtResponse generateToken(final UserEntity user){
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId(), user.getRole());
        return JwtResponse.builder()
                .grantType(JwtUtil.BEARER_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
