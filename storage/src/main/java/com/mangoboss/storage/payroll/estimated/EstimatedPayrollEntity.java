package com.mangoboss.storage.payroll.estimated;

import com.mangoboss.storage.payroll.*;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "estimated_payroll")
public class EstimatedPayrollEntity {
    @Id
    @Column(nullable = false)
    private String payrollKey; // payroll:staffId:yyyy-MM

    @Column(nullable = false)
    private Long staffId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankCode bankCode;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private LocalDate month;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WithholdingType withholdingType;

    @Column(nullable = false)
    private PayrollAmount payrollAmount;

    @Builder
    private EstimatedPayrollEntity(final String key,
                                   final Long staffId,
                                   final BankCode bankCode,
                                   final String account,
                                   final LocalDate month,
                                   final WithholdingType withholdingType,
                                   final PayrollAmount payrollAmount) {
        this.payrollKey = key;
        this.staffId = staffId;
        this.bankCode = bankCode;
        this.account = account;
        this.month = month;
        this.withholdingType = withholdingType;
        this.payrollAmount = payrollAmount;
    }

    public static EstimatedPayrollEntity create(final PayrollAmount payrollAmount,
                                                final StaffEntity staff,
                                                final LocalDate month
    ) {
        return EstimatedPayrollEntity.builder()
                .key(generateKey(staff.getId(), month))
                .staffId(staff.getId())
                .bankCode(staff.getBankCode())
                .account(staff.getAccount())
                .month(month)
                .withholdingType(staff.getWithholdingType())
                .payrollAmount(payrollAmount)
                .build();
    }

    private static String generateKey(final Long staffId, final LocalDate month) {
        String formattedMonth = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return String.format("payroll:%d:%s", staffId, formattedMonth);
    }

    public PayrollEntity createPayrollEntity(final StoreEntity store, final PayrollSettingEntity setting) {
        TransferAccountEntity transferAccount = setting.getTransferAccountEntity();
        return PayrollEntity.create(
                this.staffId,
                store.getId(),
                store.getName(),
                this.bankCode,
                this.account,
                transferAccount.getBankCode(),
                transferAccount.getAccountNumber(),
                transferAccount.getFinAccount(),
                this.month,
                this.month.plusMonths(1).withDayOfMonth(setting.getTransferDate()),
                this.withholdingType,
                this.payrollAmount
        );
    }
}