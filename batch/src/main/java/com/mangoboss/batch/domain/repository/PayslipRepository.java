package com.mangoboss.batch.domain.repository;

import com.mangoboss.storage.payroll.PayslipEntity;

public interface PayslipRepository {
    PayslipEntity save(PayslipEntity payslip);
}
