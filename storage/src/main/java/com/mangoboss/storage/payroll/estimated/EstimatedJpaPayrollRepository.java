package com.mangoboss.storage.payroll.estimated;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstimatedJpaPayrollRepository extends JpaRepository<EstimatedPayrollEntity, String> {
    EstimatedPayrollEntity save(EstimatedPayrollEntity estimatedPayroll);

    List<EstimatedPayrollEntity> findAllByPayrollKeyIn(List<String> keys);
}
