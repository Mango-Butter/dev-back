package com.mangoboss.batch.clock_out;

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
