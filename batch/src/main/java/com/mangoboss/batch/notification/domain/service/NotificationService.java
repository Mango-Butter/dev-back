package com.mangoboss.batch.notification.domain.service;

import com.mangoboss.batch.common.repository.NotificationRepository;
import com.mangoboss.batch.notification.external.FcmSender;
import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.SendStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FcmSender fcmSender;
    private final NotificationHandler notificationHandler;

    @Value("${notification.max-retry}")
    private Integer maxRetry;

    @Value("${notification.notification-batch-size}")
    private Integer batchSize;

    public void processSendableNotifications() {
        List<NotificationEntity> sendableNotifications = notificationRepository.findAllBySendStatus(
                List.of(SendStatus.PENDING, SendStatus.FAILED),
                maxRetry,
                PageRequest.of(0, batchSize));
        notificationHandler.markInProgress(sendableNotifications);
        try {
            fcmSender.send(sendableNotifications);
        } catch (Exception e) {
            log.error("FCM 전송 중 오류", e);
        }
        notificationHandler.update(sendableNotifications);
    }
}
