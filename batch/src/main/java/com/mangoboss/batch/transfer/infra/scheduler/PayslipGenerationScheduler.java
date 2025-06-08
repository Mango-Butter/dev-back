package com.mangoboss.batch.transfer.infra.scheduler;

import com.mangoboss.batch.transfer.domain.service.PayslipGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PayslipGenerationScheduler {
    private final PayslipGenerationService payslipGenerationService;

    @Scheduled(cron = "${cron.payslip}")
    public void runGeneratePayslip() {
        payslipGenerationService.generatePayslip();
    }
}
