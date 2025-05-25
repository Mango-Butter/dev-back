package com.mangoboss.batch.transfer.domain.service;

import com.mangoboss.batch.transfer.domain.repository.PayrollRepository;
import com.mangoboss.batch.transfer.domain.repository.PayslipRepository;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.payroll.TransferState;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoTransferService {
    @PersistenceContext
    private final EntityManager entityManager;

    private final PayrollRepository payrollRepository;
    private final PayslipRepository payslipRepository;
    private final TransferExecutor transferExecutor;
    private final Clock clock;

    @Value("${transfer.max-retry}")
    private Integer maxRetry;

    @Transactional
    public void autoTransfer() {
        LocalDate today = LocalDate.now(clock);

        List<PayrollEntity> payrollsForDrawing = payrollRepository.findAllByTransferDateAndTransferStateIn(
                today,
                List.of(TransferState.PENDING, TransferState.FAILED_WITHDRAW, TransferState.FAILED_TRANSFERRED),
                maxRetry,
                PageRequest.of(0, 10)
        );
        payrollsForDrawing.forEach(payroll -> {
            transferExecutor.drawingTransferWithRetry(payroll);
            transferExecutor.receivedTransferWithRetry(payroll);
            createPayslipEvent(payroll);
        });
    }

    private void createPayslipEvent(final PayrollEntity payroll) {
        if (payroll.getTransferState().equals(TransferState.COMPLETED_TRANSFERRED)) {
            PayslipEntity payslip = PayslipEntity.create(payroll);
            payslipRepository.save(payslip);
            entityManager.flush();
        }
    }
}
