package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.storage.payroll.PayrollAmount;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.WithholdingType;
import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PayrollEstimatedResponse(
        String key,
        String bankCode,
        String account,
        LocalDate month,
        WithholdingType withholdingType,
        Double totalTime,
        Integer baseAmount,
        Integer weeklyAllowance,
        Integer totalCommutingAllowance,
        Integer totalAmount,
        Integer withholdingTax,
        Integer netAmount
) {
    public static PayrollEstimatedResponse of(final EstimatedPayrollEntity estimatedPayroll) {
        PayrollAmount amount = estimatedPayroll.getPayrollAmount();
        return PayrollEstimatedResponse.builder()
                .key(estimatedPayroll.getPayrollKey())
                .bankCode(estimatedPayroll.getBankCode().getDisplayName())
                .account(estimatedPayroll.getAccount())
                .month(estimatedPayroll.getMonth())
                .withholdingType(estimatedPayroll.getWithholdingType())
                .totalTime(amount.getTotalTime())
                .baseAmount(amount.getBaseAmount())
                .weeklyAllowance(amount.getWeeklyAllowance())
                .totalCommutingAllowance(amount.getTotalCommutingAllowance())
                .totalAmount(amount.getTotalAmount())
                .withholdingTax(amount.getWithholdingTax())
                .netAmount(amount.getNetAmount())
                .build();
    }
}
