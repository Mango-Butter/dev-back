package com.mangoboss.batch.late_clock_in.infra.scheduler;

import com.mangoboss.batch.late_clock_in.domain.service.LateClockInService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LateClockInScheduler {
    private final LateClockInService lateClockInNotificationService;

    @Scheduled(cron = "${cron.clock-in}")
    public void runLateClockInNotification() {
        lateClockInNotificationService.notifyLateClockIn();
    }
}
