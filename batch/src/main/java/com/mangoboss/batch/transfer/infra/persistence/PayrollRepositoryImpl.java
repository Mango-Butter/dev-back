package com.mangoboss.batch.transfer.infra.persistence;

import com.mangoboss.batch.transfer.domain.repository.PayrollRepository;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayrollJpaRepository;
import com.mangoboss.storage.payroll.TransferState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PayrollRepositoryImpl implements PayrollRepository {
    private final PayrollJpaRepository payrollJpaRepository;

    @Override
    public List<PayrollEntity> findAllByTransferDateAndTransferStateIn(final LocalDate transferDate, final List<TransferState> transferStates,
                                                                       final Integer maxRetry, final Pageable pageable) {
        return payrollJpaRepository.findAllByTransferDateAndTransferStateIn(transferDate, transferStates, maxRetry, pageable);
    }

    @Override
    public PayrollEntity getById(Long id) {
        return payrollJpaRepository.findById(id).orElseThrow();
    }

}
