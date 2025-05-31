package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.payroll.PayslipEntity;

import java.util.Optional;

public interface PayslipRepository {

    Optional<PayslipEntity> findByPayrollId(Long payrollId);

    PayslipEntity getById(Long id);

    PayslipEntity getByIdAndStaffId(Long id, Long staffId);
}
