package com.mangoboss.batch.notification;

import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.SendStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationRepository {
    List<NotificationEntity> findAllBySendStatus(List<SendStatus> sendStatuses, Integer maxRetry, Pageable pageable);
    void save(NotificationEntity notification);
}
