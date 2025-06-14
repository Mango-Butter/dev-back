package com.mangoboss.admin.domain.service;


import com.mangoboss.admin.common.exception.CustomErrorInfo;
import com.mangoboss.admin.common.exception.CustomException;
import com.mangoboss.admin.common.security.KakaoSocialLogin;
import com.mangoboss.admin.common.util.JwtUtil;
import com.mangoboss.admin.dto.auth.requeset.LoginRequest;
import com.mangoboss.admin.dto.auth.response.JwtResponse;
import com.mangoboss.admin.dto.user.KakaoUserInfo;
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
        kakaoUserInfo.validate();
        return kakaoUserInfo;
    }

    public Long validateAndExtractIdFromRefreshToken(final String refreshToken){
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new CustomException(CustomErrorInfo.UNAUTHORIZED);
        }
        return (Long) jwtUtil.parseClaims(refreshToken).get(JwtUtil.CLAIM_USER_ID);
    }

    public JwtResponse generateToken(final UserEntity user){
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getRole());
        return JwtResponse.builder()
                .grantType(JwtUtil.BEARER_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
