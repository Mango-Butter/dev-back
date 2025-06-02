package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.notification.NotificationEntity;

import java.util.List;

public interface NotificationRepository {
    void save(NotificationEntity notification);
    List<NotificationEntity> findByUserIdAndStoreIdOrderByCreatedAtDesc(Long userId, Long storeId);
}
