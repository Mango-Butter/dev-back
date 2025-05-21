package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.payroll.PayrollEntity;

public interface PayrollRepository {
    PayrollEntity save(PayrollEntity payroll);
}
