package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.payroll.PayrollEntity;

import java.time.LocalDate;
import java.util.List;

public interface PayrollRepository {
    PayrollEntity save(PayrollEntity payroll);

    List<PayrollEntity> saveAll(List<PayrollEntity> payrolls);

    void deleteAllByStoreIdAndMonth(Long storeId, LocalDate month);

    boolean isNotTransferPending(Long storeId, LocalDate month);

    List<PayrollEntity> findAllByStoreIdAndMonth(Long storeId, LocalDate month);

    List<PayrollEntity> findAllByStoreIdAndMonthBetween(Long storeId, LocalDate start, LocalDate end);

    boolean isTransferStarted(Long storeId, LocalDate start, LocalDate end);

    PayrollEntity getById(Long id);

    PayrollEntity getByIdAndStoreId(Long id, Long storeId);

    PayrollEntity getByStaffIdAndMonthBetween(Long staffId, LocalDate start, LocalDate end);
}
