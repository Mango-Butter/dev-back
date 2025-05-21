package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.EstimatedPayrollRepository;
import com.mangoboss.storage.payroll.estimated.EstimatedJpaPayrollRepository;
import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EstimatedPayrollRepositoryImpl implements EstimatedPayrollRepository {
    private final EstimatedJpaPayrollRepository estimatedJpaPayrollRepository;

    @Override
    public EstimatedPayrollEntity save(EstimatedPayrollEntity estimatedPayroll) {
        return estimatedJpaPayrollRepository.save(estimatedPayroll);
    }
}
