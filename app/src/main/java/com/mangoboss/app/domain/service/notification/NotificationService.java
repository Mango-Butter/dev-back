package com.mangoboss.app.domain.service.notification;

import com.mangoboss.app.domain.repository.NotificationRepository;
import com.mangoboss.storage.notification.NotificationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public List<NotificationEntity> getNotificationsByUserAndStore(final Long userId, final Long storeId) {
        return notificationRepository.findByUserIdAndStoreIdOrderByCreatedAtDesc(userId, storeId);
    }

}
