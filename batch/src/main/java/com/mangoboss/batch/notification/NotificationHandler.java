package com.mangoboss.batch.notification;

import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.SendStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationHandler {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void update(List<NotificationEntity> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void markInProgress(List<NotificationEntity> notifications) {
        List<Long> ids = notifications.stream()
                .map(NotificationEntity::getId)
                .collect(Collectors.toList());

        notificationRepository.updateSendStatus(ids, SendStatus.IN_PROGRESS);
    }
}
