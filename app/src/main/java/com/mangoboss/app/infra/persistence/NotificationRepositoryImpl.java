package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.NotificationRepository;
import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.NotificationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public void save(NotificationEntity notification) {
        notificationJpaRepository.save(notification);
    }

    @Override
    public List<NotificationEntity> findByUserIdAndStoreIdOrderByCreatedAtDesc(final Long userId, final Long storeId) {
        return notificationJpaRepository.findByUserIdAndStoreIdOrderByCreatedAtDesc(userId, storeId);
    }
}
