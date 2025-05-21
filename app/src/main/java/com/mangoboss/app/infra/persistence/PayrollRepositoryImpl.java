package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.PayrollRepository;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayrollJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PayrollRepositoryImpl implements PayrollRepository {
    private final PayrollJpaRepository payrollJpaRepository;

    @Override
    public PayrollEntity save(final PayrollEntity payroll) {
        return payrollJpaRepository.save(payroll);
    }

    @Override
    public List<PayrollEntity> saveAll(final List<PayrollEntity> payrolls) {
        return payrollJpaRepository.saveAll(payrolls);
    }

    @Override
    public void deleteAllByStoreIdAndMonth(final Long storeId, final LocalDate month) {
        payrollJpaRepository.deleteAllByStoreIdAndMonth(storeId, month);
    }
}
