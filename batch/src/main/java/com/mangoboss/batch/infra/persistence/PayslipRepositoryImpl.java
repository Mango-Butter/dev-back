package com.mangoboss.batch.infra.persistence;

import com.mangoboss.batch.domain.repository.PayslipRepository;
import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.payroll.PayslipJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PayslipRepositoryImpl implements PayslipRepository {
    private final PayslipJpaRepository payslipJpaRepository;


    @Override
    public PayslipEntity save(PayslipEntity payslip) {
        return payslipJpaRepository.save(payslip);
    }
}
