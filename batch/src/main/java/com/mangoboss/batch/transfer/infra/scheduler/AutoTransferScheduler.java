package com.mangoboss.batch.transfer.infra.scheduler;

import com.mangoboss.batch.transfer.domain.service.AutoTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoTransferScheduler {
    private final AutoTransferService autoTransferService;

    @Scheduled(cron = "${cron.transfer}")
    public void runTransfer() {
        autoTransferService.autoTransfer();
    }
}
