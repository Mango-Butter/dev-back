package com.mangoboss.batch.infra.persistence;

import com.mangoboss.batch.domain.repository.PayrollRepository;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayrollJpaRepository;
import com.mangoboss.storage.payroll.TransferState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PayrollRepositoryImpl implements PayrollRepository {
    private final PayrollJpaRepository payrollJpaRepository;

    @Override
    public List<PayrollEntity> findAllByTransferDateAndTransferStateIn(LocalDate transferDate, List<TransferState> transferStates) {
        return payrollJpaRepository.findAllByTransferDateAndTransferStateIn(transferDate, transferStates);
    }

    @Override
    public PayrollEntity getById(Long id) {
        return payrollJpaRepository.findById(id).orElseThrow();
    }

}
