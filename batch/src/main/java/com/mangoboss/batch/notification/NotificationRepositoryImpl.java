package com.mangoboss.batch.notification;

import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.NotificationJpaRepository;
import com.mangoboss.storage.notification.SendStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public List<NotificationEntity> findAllBySendStatus(final List<SendStatus> sendStatuses, final Integer maxRetry, final Pageable pageable) {
        return notificationJpaRepository.findSendableNotifications(sendStatuses, maxRetry, pageable);
    }

    @Override
    public void saveAll(final List<NotificationEntity> notifications) {
        notificationJpaRepository.saveAll(notifications);
    }

    @Override
    public void updateSendStatus(List<Long> notificationIds, SendStatus status) {
        notificationJpaRepository.updateSendStatus(notificationIds, status);
    }
}
