package com.mangoboss.storage.notification;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("""
                SELECT n FROM NotificationEntity n
                WHERE 
                  n.sendStatus IN :sendStatuses
                  AND n.retryCount < :maxRetry
            """)
    List<NotificationEntity> findSendableNotifications(
            @Param("sendStatuses") List<SendStatus> sendStatuses,
            @Param("maxRetry") Integer maxRetry,
            Pageable pageable
    );

}
