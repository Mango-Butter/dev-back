package com.mangoboss.storage.payroll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PayrollJpaRepository extends JpaRepository<PayrollEntity, Long> {
    List<PayrollEntity> findAllByTransferDateAndTransferStateIn(LocalDate transferDate, List<TransferState> transferStates);
}
