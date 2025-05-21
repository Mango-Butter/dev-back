package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.payroll.PayrollEntity;

import java.time.LocalDate;
import java.util.List;

public interface PayrollRepository {
    PayrollEntity save(PayrollEntity payroll);

    List<PayrollEntity> saveAll(List<PayrollEntity> payrolls);

    void deleteAllByStoreIdAndMonth(Long storeId, LocalDate month);
}
