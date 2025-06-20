package com.mangoboss.storage.notification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("""
            SELECT n FROM NotificationEntity n
            WHERE
              n.sendStatus IN :sendStatuses
              AND n.retryCount < :maxRetry
              AND n.targetToken IS NOT NULL
            """)
    List<NotificationEntity> findSendableNotifications(
            @Param("sendStatuses") List<SendStatus> sendStatuses,
            @Param("maxRetry") Integer maxRetry,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.sendStatus = :status WHERE n.id IN :ids")
    void updateSendStatus(@Param("ids") List<Long> ids, @Param("status") SendStatus status);

    List<NotificationEntity> findByUserIdAndStoreIdOrderByCreatedAtDesc(Long userId, Long storeId);

}
