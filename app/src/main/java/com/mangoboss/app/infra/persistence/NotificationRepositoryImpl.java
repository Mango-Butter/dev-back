package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.NotificationRepository;
import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public void save(NotificationEntity notification) {
        notificationJpaRepository.save(notification);
    }
}
