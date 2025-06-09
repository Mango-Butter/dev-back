package com.mangoboss.batch.common.persistence;

import com.mangoboss.batch.common.repository.PayslipRepository;
import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.payroll.PayslipJpaRepository;
import com.mangoboss.storage.payroll.PayslipState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PayslipRepositoryImpl implements PayslipRepository {
    private final PayslipJpaRepository payslipJpaRepository;


    @Override
    public PayslipEntity save(final PayslipEntity payslip) {
        return payslipJpaRepository.save(payslip);
    }

    @Override
    public List<PayslipEntity> findAllByPayslipState(final List<PayslipState> payslipStates, final Integer maxRetry, final Pageable pageable) {
        return payslipJpaRepository.findAllByPayslipState(payslipStates, maxRetry, pageable);
    }
}
