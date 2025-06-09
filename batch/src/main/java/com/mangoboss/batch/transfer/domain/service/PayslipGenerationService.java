package com.mangoboss.batch.transfer.domain.service;

import com.mangoboss.batch.common.exception.CustomErrorInfo;
import com.mangoboss.batch.common.repository.PayslipRepository;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.payroll.PayslipState;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PayslipGenerationService {
    private final PayslipRepository payslipRepository;
    private final PayslipGenerator payslipExecutor;

    @PersistenceContext
    private final EntityManager entityManager;

    @Value("${transfer.max-retry}")
    private Integer maxRetry;

    @Value("${transfer.payslip-batch-size}")
    private Integer batchSize;

    @Transactional
    public void generatePayslip() {
        List<PayslipEntity> payslips = payslipRepository.findAllByPayslipState(
                List.of(PayslipState.PENDING, PayslipState.FAILED),
                maxRetry,
                PageRequest.of(0, batchSize)
        );
        payslips.forEach(
                payslip -> {
                    PayrollEntity payroll = payslip.getPayroll();
                    try {
                        String html = payslipExecutor.renderPayslipHtml(payroll);
                        byte[] pdfBytes = payslipExecutor.generatePdfFromHtml(html);
                        String fileKey = payslipExecutor.savePayslipPdf(pdfBytes);
                        payslip.saveFileKey(fileKey);
                        entityManager.flush();
                    } catch (Exception e) {
                        log.warn("[{}] payslipId = {}", CustomErrorInfo.PDF_GENERATION_FAILED.getMessage(), payslip.getId(), e);
                    }
                }
        );
    }
}
