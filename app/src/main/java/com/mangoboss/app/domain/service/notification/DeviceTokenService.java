package com.mangoboss.app.domain.service.notification;

import com.mangoboss.app.domain.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    @Transactional
    public void registerDeviceToken(final Long userId, final String tokenValue) {
        final boolean exists = deviceTokenRepository.existsByUserIdAndTokenValue(userId, tokenValue);

        if (exists) {
            deviceTokenRepository.updateIsDeletedFalseAndModifiedAt(userId, tokenValue);
        } else {
            deviceTokenRepository.save(userId, tokenValue);
        }
    }
}