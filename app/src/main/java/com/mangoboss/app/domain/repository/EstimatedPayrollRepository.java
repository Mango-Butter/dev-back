package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;

import java.util.List;

public interface EstimatedPayrollRepository {
    EstimatedPayrollEntity save(EstimatedPayrollEntity estimatedPayroll);

    List<EstimatedPayrollEntity> findAllByPayrollKeyIn(List<String> keys);
}
