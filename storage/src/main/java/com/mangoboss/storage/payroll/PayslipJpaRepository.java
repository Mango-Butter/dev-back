package com.mangoboss.storage.payroll;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PayslipJpaRepository extends JpaRepository<PayslipEntity, Long> {
    List<PayslipEntity> findAllByPayslipState(PayslipState state);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p FROM PayslipEntity p
            WHERE p.payslipState = :state AND p.retryCount < :maxRetry
            ORDER BY p.createdAt ASC
            """)
    List<PayrollEntity> findAllByPayslipState(
            @Param("payslipState") PayslipState state,
            @Param("maxRetry") Integer maxRetry,
            Pageable pageable
    );
}
