package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.EstimatedPayrollRepository;
import com.mangoboss.storage.payroll.estimated.EstimatedJpaPayrollRepository;
import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EstimatedPayrollRepositoryImpl implements EstimatedPayrollRepository {
    private final EstimatedJpaPayrollRepository estimatedJpaPayrollRepository;

    @Override
    public EstimatedPayrollEntity save(EstimatedPayrollEntity estimatedPayroll) {
        return estimatedJpaPayrollRepository.save(estimatedPayroll);
    }

    @Override
    public List<EstimatedPayrollEntity> findAllByPayrollKeyIn(List<String> keys) {
        return estimatedJpaPayrollRepository.findAllByPayrollKeyIn(keys);
    }
}
