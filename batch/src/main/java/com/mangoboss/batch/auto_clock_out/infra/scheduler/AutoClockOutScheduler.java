package com.mangoboss.batch.auto_clock_out.infra.scheduler;

import com.mangoboss.batch.auto_clock_out.domain.service.AutoClockOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoClockOutScheduler {
    private final AutoClockOutService autoClockOutService;

    @Scheduled(cron = "${cron.clock-out}")
    public void runAutoClockOut() {
        autoClockOutService.autoClockOut();
    }
}
