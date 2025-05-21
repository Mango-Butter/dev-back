package com.mangoboss.storage.payroll.estimated;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimatedJpaPayrollRepository extends JpaRepository<EstimatedPayrollEntity, String> {
    EstimatedPayrollEntity save(EstimatedPayrollEntity estimatedPayroll);
}
