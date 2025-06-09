package com.mangoboss.batch.late_clock_in.domain.service;

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
public class NotificationLateClockInService {
    private final NotificationRepository notificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    @Value("${frontend-url}")
    private String frontendUrl;

    private List<NotificationEntity> generateNotification(final Long userId, final Long storeId, final Long scheduleId, final String title,
                                                          final String content, final NotificationType type, final String path) {
        String clickUrl = frontendUrl + path;
        List<String> tokens = deviceTokenRepository.findActiveTokensByUserId(userId);
        if (tokens.isEmpty()) {
            final NotificationEntity notification = NotificationEntity.createWithMetaId(userId, storeId, title, content, null, clickUrl, type, null, scheduleId);
            return List.of(notification);
        }
        return tokens.stream()
                .map(token -> NotificationEntity.createWithMetaId(userId, storeId, title, content, null, clickUrl, type, token, scheduleId))
                .toList();
    }

    private List<NotificationEntity> generateLateClockInNotification(final Long userId, final Long storeId, final String staffName, final Long scheduleId) {
        String content = String.format("%s님이 출근 시간으로부터 10분이 지나도 출근하지 않았어요.", staffName);
        return generateNotification(
                userId,
                storeId,
                scheduleId,
                "지각 알림",
                content,
                NotificationType.SCHEDULE,
                "/boss/schedule"
        );
    }

    @Transactional
    public void saveNotifications(final List<ScheduleForNotificationProjection> projections) {
        List<NotificationEntity> notifications = projections.stream()
                .flatMap(schedule -> generateLateClockInNotification(
                        schedule.getBossId(),
                        schedule.getStoreId(),
                        schedule.getStaffName(),
                        schedule.getSchedule().getId()
                ).stream())
                .toList();
        notificationRepository.saveAll(notifications);
    }
}
