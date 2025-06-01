package com.mangoboss.batch.notification;

import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.SendStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmSender fcmSender;

    @Value("${notification.max-retry}")
    private Integer maxRetry;

    @Value("${notification.notification-batch-size}")
    private Integer batchSize;

    @Transactional
    public void processSendableNotifications() {
        List<NotificationEntity> sendableNotifications = notificationRepository.findAllBySendStatus(
                List.of(SendStatus.PENDING, SendStatus.FAILED),
                maxRetry,
                PageRequest.of(0, batchSize));
        fcmSender.send(sendableNotifications);
        sendableNotifications.forEach(notificationRepository::save);
    }
}
