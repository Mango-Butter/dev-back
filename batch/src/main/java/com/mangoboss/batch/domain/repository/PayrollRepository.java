package com.mangoboss.batch.domain.repository;

import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.TransferState;

import java.time.LocalDate;
import java.util.List;

public interface PayrollRepository {
    List<PayrollEntity> findAllByTransferDateAndTransferStateIn(LocalDate transferDate, List<TransferState> transferStates);

    PayrollEntity getById(Long id);
}
