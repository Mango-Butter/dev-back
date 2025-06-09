package com.mangoboss.batch.auto_clock_out.domain.service;

import com.mangoboss.batch.common.repository.DeviceTokenRepository;
import com.mangoboss.batch.common.repository.NotificationRepository;
import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.NotificationType;
import com.mangoboss.storage.schedule.projection.ScheduleForNotificationProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationAutoClockOutService {
    private final NotificationRepository notificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    @Value("${frontend-url}")
    private String frontendUrl;


    private List<NotificationEntity> generateNotification(final Long userId, final Long storeId, final String title,
                                                          final String content, final NotificationType type, final String path) {
        String clickUrl = frontendUrl + path;
        List<String> tokens = deviceTokenRepository.findActiveTokensByUserId(userId);
        if (tokens.isEmpty()) {
            final NotificationEntity notification = NotificationEntity.create(userId, storeId, title, content, null, clickUrl, type, null);
            return List.of(notification);
        }
        return tokens.stream()
                .map(token -> NotificationEntity.create(userId, storeId, title, content, null, clickUrl, type, token))
                .toList();
    }

    private List<NotificationEntity> generateAbsentClockInNotification(final Long userId, final Long storeId, final String staffName) {
        String content = String.format("%s님이 결근처리 되었어요.", staffName);
        return generateNotification(
                userId,
                storeId,
                "결근 알림",
                content,
                NotificationType.SCHEDULE,
                "/boss/schedule"
        );
    }

    @Transactional
    public void saveNotifications(final List<ScheduleForNotificationProjection> projections) {
        List<NotificationEntity> notifications = projections.stream()
                .flatMap(schedule -> generateAbsentClockInNotification(
                        schedule.getBossId(),
                        schedule.getStoreId(),
                        schedule.getStaffName()
                ).stream())
                .toList();
        notificationRepository.saveAll(notifications);
    }
}
