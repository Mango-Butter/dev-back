package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;

public interface EstimatedPayrollRepository {
    EstimatedPayrollEntity save(EstimatedPayrollEntity estimatedPayroll);
}
