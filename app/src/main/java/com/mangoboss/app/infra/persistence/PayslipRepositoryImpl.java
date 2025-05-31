package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.PayslipRepository;
import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.payroll.PayslipJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PayslipRepositoryImpl implements PayslipRepository {
    private final PayslipJpaRepository payslipJpaRepository;

    @Override
    public Optional<PayslipEntity> findByPayrollId(final Long payrollId) {
        return payslipJpaRepository.findByPayrollId(payrollId);
    }

    @Override
    public PayslipEntity getById(final Long id) {
        return payslipJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.PAYSLIP_NOT_FOUND));
    }
}
