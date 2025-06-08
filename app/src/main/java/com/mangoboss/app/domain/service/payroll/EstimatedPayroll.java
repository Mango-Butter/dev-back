package com.mangoboss.app.domain.service.payroll;

import com.mangoboss.storage.payroll.*;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EstimatedPayroll {
    private String key;
    private Long staffId;
    private String staffName;
    private BankCode bankCode;
    private String account;
    private LocalDate month;
    private WithholdingType withholdingType;
    private Double totalTime;
    private Integer baseAmount;
    private Integer weeklyAllowance;
    private Integer totalCommutingAllowance;
    private Integer totalAmount;
    private Integer withholdingTax;
    private Integer netAmount;

    public static EstimatedPayroll create(final PayrollAmount payrollAmount,
                                          final StaffEntity staff,
                                          final LocalDate month
    ) {
        return EstimatedPayroll.builder()
                .key(generateKey(staff.getId(), month))
                .staffId(staff.getId())
                .staffName(staff.getName())
                .bankCode(staff.getBankCode())
                .account(staff.getAccountNumber())
                .month(month)
                .withholdingType(staff.getWithholdingType())
                .totalTime(payrollAmount.getTotalTime())
                .baseAmount(payrollAmount.getBaseAmount())
                .weeklyAllowance(payrollAmount.getWeeklyAllowance())
                .totalCommutingAllowance(payrollAmount.getTotalCommutingAllowance())
                .totalAmount(payrollAmount.getTotalAmount())
                .withholdingTax(payrollAmount.getWithholdingTax())
                .netAmount(payrollAmount.getNetAmount())
                .build();
    }

    private static String generateKey(final Long staffId, final LocalDate month){
        String formattedMonth = month.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return String.format("payroll:%d:%s", staffId, formattedMonth);
    }

    public PayrollEntity createPayroll(final StoreEntity store, final PayrollSettingEntity payrollSetting) {
        TransferAccountEntity transferAccount = payrollSetting.getTransferAccountEntity();
        PayrollAmount payrollAmount = PayrollAmount.create(
                this.totalTime,
                this.baseAmount,
                this.weeklyAllowance,
                this.totalCommutingAllowance,
                this.totalAmount,
                this.withholdingTax,
                this.netAmount
        );
        return PayrollEntity.create(
                this.staffId,
                store.getId(),
                store.getName(),
                store.getBusinessNumber(),
                this.staffName,
                this.bankCode,
                this.account,
                store.getBoss().getName(),
                transferAccount.getBankCode(),
                transferAccount.getAccountNumber(),
                transferAccount.getFinAccount(),
                this.month,
                this.month.plusMonths(1).withDayOfMonth(payrollSetting.getTransferDate()),
                this.withholdingType,
                payrollAmount
        );

    }
}