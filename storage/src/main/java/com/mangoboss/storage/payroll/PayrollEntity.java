package com.mangoboss.storage.payroll;

import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

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

    private String taxDeductions;

    @Column(nullable = false)
    private Integer netAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferState transferState;

    @Column(nullable = false)
    private String finAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankCode withdrawalBankCode;

    @Column(nullable = false)
    private String withdrawalAccount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankCode depositBankCode;

    @Column(nullable = false)
    private String depositAccount;

    @Column(nullable = false)
    private Integer retryCount = 0;

    private LocalDateTime transferredAt;

    private String payslipUrl;

    @Builder
    private PayrollEntity(final String month, final LocalDate transferDate, final LocalDate periodStart,
                          final LocalDate periodEnd, final Integer totalAmount, final String taxDeductions,
                          final Integer netAmount, final TransferState transferState, final String finAccount,
                          final BankCode withdrawalBankCode, final String withdrawalAccountNumber,
                          final BankCode depositBankCode, final String depositAccountNumber,
                          final StaffEntity staff, final StoreEntity store) {
        this.month = month;
        this.transferDate = transferDate;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.totalAmount = totalAmount;
        this.taxDeductions = taxDeductions;
        this.netAmount = netAmount;
        this.transferState = transferState;
        this.finAccount = finAccount;
        this.withdrawalBankCode = withdrawalBankCode;
        this.withdrawalAccount = withdrawalAccountNumber;
        this.depositBankCode = depositBankCode;
        this.depositAccount = depositAccountNumber;
        this.staff = staff;
        this.store = store;
    }

    public static PayrollEntity create(final String month, final LocalDate transferDate, final LocalDate periodStart,
                                       final LocalDate periodEnd, final Integer totalAmount, final String taxDeductions,
                                       final Integer netAmount, final String finAccount,
                                       final BankCode withdrawalBankCode, final String withdrawalAccountNumber,
                                       final BankCode depositBankCode, final String depositAccountNumber,
                                       final StaffEntity staff, final StoreEntity store) {
        return PayrollEntity.builder()
                .month(month)
                .transferDate(transferDate)
                .periodStart(periodStart)
                .periodEnd(periodEnd)
                .totalAmount(totalAmount)
                .taxDeductions(taxDeductions)
                .netAmount(netAmount)
                .transferState(TransferState.PENDING)
                .finAccount(finAccount)
                .withdrawalBankCode(withdrawalBankCode)
                .withdrawalAccountNumber(withdrawalAccountNumber)
                .depositBankCode(depositBankCode)
                .depositAccountNumber(depositAccountNumber)
                .staff(staff)
                .store(store)
                .build();
    }

    public void markWithdrawn() {
        this.transferState = TransferState.REQUESTED_WITHDRAWN;
    }

    public void markTransferred() {
        this.transferState = TransferState.REQUESTED_TRANSFERRED;
    }

    public void markCompleted(final LocalDateTime transferredAt) {
        this.transferState = transferState.markCompleted();
        this.transferredAt = transferredAt;
    }

    public void markFailed() {
        this.transferState = transferState.markFailed();
        this.retryCount++;
    }
}
