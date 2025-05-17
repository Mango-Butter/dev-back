package com.mangoboss.storage.payroll;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "payroll",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_staff_month",
                columnNames = {"staff_id", "month"}
        ))
public class PayrollEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_id")
    private Long id;

    @Column(nullable = false, length = 7)
    @Length(min = 7, max = 7)
    private String month;

    @Column(nullable = false)
    private LocalDate transferDate;

    @Column(nullable = false)
    private LocalDate periodStart;

    @Column(nullable = false)
    private LocalDate periodEnd;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column(columnDefinition = "jsonb")
    private String taxDeductions;

    @Column(nullable = false)
    private Integer netAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferState transferState;

    @Column(nullable = false)
    private Integer retryCount = 0;

    private LocalDateTime transferredAt;

    private String payslipUrl;

    @Builder
    private PayrollEntity(final String month, final LocalDate transferDate, final LocalDate periodStart,
                          final LocalDate periodEnd, final Integer totalAmount, final String taxDeductions,
                          final Integer netAmount, final TransferState transferState) {
        this.month = month;
        this.transferDate = transferDate;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.totalAmount = totalAmount;
        this.taxDeductions = taxDeductions;
        this.netAmount = netAmount;
        this.transferState = transferState;
    }

    public static PayrollEntity create(final String month, final LocalDate transferDate, final LocalDate periodStart,
                                       final LocalDate periodEnd, final Integer totalAmount, final String taxDeductions,
                                       final Integer netAmount) {
        return PayrollEntity.builder()
                .month(month)
                .transferDate(transferDate)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .totalAmount(totalAmount)
                .taxDeductions(taxDeductions)
                .netAmount(netAmount)
                .transferState(TransferState.PENDING)
                .build();
    }

    public void increaseRetryCount() {
        this.retryCount++;
    }

    public void markRequested() {
        this.transferState = TransferState.REQUESTED;
    }

    public void markCompleted() {
        this.transferState = TransferState.COMPLETED;
    }

    public void markFailed() {
        this.transferState = TransferState.FAILED;
    }
}
