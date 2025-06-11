package com.mangoboss.app.domain.service.notification;

import com.mangoboss.app.domain.repository.DeviceTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceTokenServiceTest {

    @InjectMocks
    private DeviceTokenService deviceTokenService;

    @Mock
    private DeviceTokenRepository deviceTokenRepository;

    @Test
    void 이미존재하는_토큰이면_업데이트만() {
        Long userId = 1L;
        String token = "token-123";

        when(deviceTokenRepository.existsByUserIdAndTokenValue(userId, token)).thenReturn(true);

        deviceTokenService.registerDeviceToken(userId, token);

        verify(deviceTokenRepository).updateIsDeletedFalseAndModifiedAt(userId, token);
        verify(deviceTokenRepository, never()).save(any(), any());
    }

    @Test
    void 존재하지않는_토큰이면_새로저장() {
        Long userId = 2L;
        String token = "token-456";

        when(deviceTokenRepository.existsByUserIdAndTokenValue(userId, token)).thenReturn(false);

        deviceTokenService.registerDeviceToken(userId, token);

        verify(deviceTokenRepository).save(userId, token);
        verify(deviceTokenRepository, never()).updateIsDeletedFalseAndModifiedAt(any(), any());
    }
}