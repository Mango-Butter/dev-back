package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.notification.NotificationEntity;

public interface NotificationRepository {
    void save(NotificationEntity notification);
}
