package com.mangoboss.batch.notification.infra.scheduler;

import com.mangoboss.batch.notification.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final NotificationService notificationService;

    @Scheduled(cron = "${cron.notification}")
    public void run() {
        notificationService.processSendableNotifications();
    }
}
