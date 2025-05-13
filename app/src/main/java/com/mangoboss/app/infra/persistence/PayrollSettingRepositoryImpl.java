package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.PayrollSettingRepository;
import com.mangoboss.storage.payroll.PayrollSettingEntity;
import com.mangoboss.storage.payroll.PayrollSettingJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PayrollSettingRepositoryImpl implements PayrollSettingRepository {
    private final PayrollSettingJpaRepository payrollSettingJpaRepository;

    @Override
    public void save(PayrollSettingEntity payrollSetting) {
        payrollSettingJpaRepository.save(payrollSetting);
    }
}
