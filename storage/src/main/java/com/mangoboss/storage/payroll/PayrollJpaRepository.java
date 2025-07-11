package com.mangoboss.storage.payroll;

import com.mangoboss.storage.payroll.projection.PayrollWithPayslipProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PayrollJpaRepository extends JpaRepository<PayrollEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p FROM PayrollEntity p
            WHERE p.transferDate = :today AND p.transferState IN :transferStates AND p.retryCount < :maxRetry
            ORDER BY p.createdAt ASC
            """)
    List<PayrollEntity> findAllByTransferDateAndTransferStateIn(@Param("today") LocalDate today,
                                                                @Param("transferStates") List<TransferState> transferStates,
                                                                @Param("maxRetry") Integer maxRetry,
                                                                Pageable pageable);

    Boolean existsByStoreIdAndMonthAndTransferStateNot(Long storeId, LocalDate month, TransferState transferState);

    void deleteAllByStoreIdAndMonth(Long storeId, LocalDate month);

    List<PayrollEntity> getAllByStoreIdAndMonth(Long storeId, LocalDate month);

    List<PayrollEntity> findAllByStoreIdAndMonthBetween(Long storeId, LocalDate start, LocalDate end);

    boolean existsByStoreIdAndMonthBetweenAndTransferStateNot(
            Long storeId, LocalDate start, LocalDate end, TransferState state);

    Optional<PayrollEntity> findByIdAndStoreId(Long payrollId, Long storeId);

    Optional<PayrollEntity> findByStaffIdAndMonthBetween(
            Long staffId, LocalDate start, LocalDate end);
}
