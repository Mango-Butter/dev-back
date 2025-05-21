package com.mangoboss.storage.payroll;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PayrollJpaRepository extends JpaRepository<PayrollEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PayrollEntity p WHERE p.transferDate = :today AND p.transferState IN :transferStates")
    List<PayrollEntity> findAllByTransferDateAndTransferStateIn(@Param("today") LocalDate today,
                                                                @Param("transferStates") List<TransferState> transferStates);

    void deleteAllByStoreIdAndMonth(Long storeId, LocalDate month);
}
