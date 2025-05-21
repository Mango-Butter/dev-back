package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.PayrollRepository;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayrollJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PayrollRepositoryImpl implements PayrollRepository {
    private final PayrollJpaRepository payrollJpaRepository;

    @Override
    public PayrollEntity save(final PayrollEntity payroll) {
        return payrollJpaRepository.save(payroll);
    }
}
