package com.mangoboss.batch.infra.scheduler;

import com.mangoboss.batch.domain.service.AutoClockOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoClockOutScheduler {
    private final AutoClockOutService autoClockOutService;

//    @Scheduled(cron = "0 */1 * * * *")
    @Scheduled(cron = "*/30 * * * * *") // 30초
    public void runAutoClockOut() {
        autoClockOutService.autoClockOut();
    }
}
