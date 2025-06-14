package com.mangoboss.app.api.facade.user;

import com.mangoboss.app.domain.service.auth.AuthService;
import com.mangoboss.app.domain.service.notification.DeviceTokenService;
import com.mangoboss.app.dto.auth.response.JwtResponse;
import com.mangoboss.app.dto.user.request.DeviceTokenRegisterRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.user.response.UserInfoResponse;
import com.mangoboss.app.dto.auth.requeset.SignUpRequest;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;
    private final AuthService authService;
    private final DeviceTokenService deviceTokenService;

    @Transactional
    public UserInfoResponse getUserInfo(final CustomUserDetails userDetails) {
        UserEntity user = userService.getUserById(userDetails.getUserId());
        return UserInfoResponse.fromEntity(user);
    }

    public JwtResponse signUp(final Long userId, final SignUpRequest request) {
        final UserEntity user = userService.getUserById(userId);
        userService.signUp(user, request.role());
        return authService.generateToken(user);
    }

    public void registerDeviceToken(final Long userId, final DeviceTokenRegisterRequest request) {
        deviceTokenService.registerDeviceToken(userId, request.fcmToken());
    }
}
