package com.mangoboss.batch.common.repository;

import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.payroll.PayslipState;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PayslipRepository {
    PayslipEntity save(PayslipEntity payslip);

    List<PayslipEntity> findAllByPayslipState(List<PayslipState> payslipStates, Integer maxRetry, Pageable pageable);
}
