package com.mangoboss.batch.billing;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoBillingScheduler {
    private final AutoBillingService autoBillingService;

    @Scheduled(cron = "${cron.billing}")
    public void runBilling() {
        autoBillingService.autoBilling();
    }
}