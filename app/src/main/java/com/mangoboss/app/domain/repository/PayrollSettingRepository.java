package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.payroll.PayrollSettingEntity;

public interface PayrollSettingRepository {
    void save(PayrollSettingEntity payrollSetting);
}
