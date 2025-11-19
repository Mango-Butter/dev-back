package com.mangoboss.storage.payroll;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PayslipJpaRepository extends JpaRepository<PayslipEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT p FROM PayslipEntity p
            WHERE p.payslipState IN :payslipStates AND p.retryCount < :maxRetry
            ORDER BY p.createdAt ASC
            """)
    List<PayslipEntity> findAllByPayslipState(
            @Param("payslipStates") List<PayslipState> states,
            @Param("maxRetry") Integer maxRetry,
            Pageable pageable
    );

    Optional<PayslipEntity> findByPayrollId(Long payrollId);

    @Query("""
            SELECT s
            FROM PayslipEntity s
            WHERE s.id = :id AND s.payroll.staffId = :staffId
            """)
    Optional<PayslipEntity> findByIdAndStaffId(@Param("id") Long id, @Param("staffId") Long staffId);
}
