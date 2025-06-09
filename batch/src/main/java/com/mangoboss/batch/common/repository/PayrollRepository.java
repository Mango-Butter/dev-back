package com.mangoboss.batch.common.repository;

import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.TransferState;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PayrollRepository {
    List<PayrollEntity> findAllByTransferDateAndTransferStateIn(LocalDate transferDate, List<TransferState> transferStates,
                                                                Integer maxRetry, Pageable pageable);

    PayrollEntity getById(Long id);
}
