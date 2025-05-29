package com.mangoboss.storage.payroll;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
public class PayrollEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_id")
    private Long id;

    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private String storeName;

    @Column(nullable = false)
    private String businessNumber;

    @Column(nullable = false)
    private String staffName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankCode depositBankCode;

    @Column(nullable = false)
    private String depositAccount;

    @Column(nullable = false)
    private String bossName;

    @Column(nullable = false)
    private BankCode withdrawalBankcode;

    @Column(nullable = false)
    private String withdrawalAccount;

    @Column(nullable = false)
    private String finAccount;

    @Column(nullable = false)
    private LocalDate month;

    @Column(nullable = false)
    private LocalDate transferDate;

    @Column(nullable = false)
    private WithholdingType withholdingType;

    @Column(nullable = false)
    @Embedded
    private PayrollAmount payrollAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferState transferState;

    @Column(nullable = false)
    private Integer retryCount;

    private LocalDateTime transferredAt;

    private String payslipUrl;

    @Builder
    private PayrollEntity(
            final Long staffId,
            final Long storeId,
            final String storeName,
            final String businessNumber,
            final String staffName,
            final BankCode depositBankCode,
            final String depositAccount,
            final String bossName,
            final BankCode withdrawlBankcode,
            final String withdrawalAccount,
            final String finAccount,
            final LocalDate month,
            final LocalDate transferDate,
            final WithholdingType withholdingType,
            final PayrollAmount payrollAmount,
            final TransferState transferState,
            final Integer retryCount
    ) {
        this.staffId = staffId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.businessNumber = businessNumber;
        this.staffName = staffName;
        this.depositBankCode = depositBankCode;
        this.depositAccount = depositAccount;
        this.bossName = bossName;
        this.withdrawalBankcode = withdrawlBankcode;
        this.withdrawalAccount = withdrawalAccount;
        this.finAccount = finAccount;
        this.month = month;
        this.transferDate = transferDate;
        this.withholdingType = withholdingType;
        this.payrollAmount = payrollAmount;
        this.transferState = transferState;
        this.retryCount = retryCount;
    }

    public static PayrollEntity create(
            final Long staffId,
            final Long storeId,
            final String storeName,
            final String businessNumber,
            final String staffName,
            final BankCode depositBankCode,
            final String depositAccount,
            final String bossName,
            final BankCode withdrawlBankcode,
            final String withdrawalAccount,
            final String finAccount,
            final LocalDate month,
            final LocalDate transferDate,
            final WithholdingType withholdingType,
            final PayrollAmount payrollAmount
    ) {
        return PayrollEntity.builder()
                .staffId(staffId)
                .storeId(storeId)
                .storeName(storeName)
                .businessNumber(businessNumber)
                .staffName(staffName)
                .depositBankCode(depositBankCode)
                .depositAccount(depositAccount)
                .bossName(bossName)
                .withdrawlBankcode(withdrawlBankcode)
                .withdrawalAccount(withdrawalAccount)
                .finAccount(finAccount)
                .month(month)
                .transferDate(transferDate)
                .withholdingType(withholdingType)
                .payrollAmount(payrollAmount)
                .transferState(TransferState.PENDING)
                .retryCount(0)
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
