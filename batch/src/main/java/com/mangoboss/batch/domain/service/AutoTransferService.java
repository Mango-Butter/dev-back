package com.mangoboss.batch.domain.service;

import com.mangoboss.batch.domain.repository.PayrollRepository;
import com.mangoboss.batch.domain.repository.PayslipRepository;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.payroll.TransferState;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    public void autoTransfer() {
        LocalDate today = LocalDate.now(clock);

        List<PayrollEntity> payrollsForDrawing = payrollRepository.findAllByTransferDateAndTransferStateIn(today,
                List.of(TransferState.PENDING, TransferState.FAILED_WITHDRAW, TransferState.FAILED_TRANSFERRED));
        payrollsForDrawing.forEach(payroll -> {
            transferExecutor.drawingTransferWithRetry(payroll);
            transferExecutor.receivedTransferWithRetry(payroll);
            createPayslipEvent(payroll);
        });
    }

    private void createPayslipEvent(final PayrollEntity payroll){
        if(payroll.getTransferState().equals(TransferState.COMPLETED_TRANSFERRED)){
            PayslipEntity payslip = PayslipEntity.create(payroll);
            payslipRepository.save(payslip);
            entityManager.flush();
        }
    }
}
