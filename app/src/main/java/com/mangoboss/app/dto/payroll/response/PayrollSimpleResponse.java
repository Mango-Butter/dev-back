package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.storage.payroll.PayrollAmount;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PayrollSimpleResponse(
        String key,
        String bankCode,
        String account,
        LocalDate month,
        String withholdingType,
        Double totalTime,
        Integer baseAmount,
        Integer weeklyAllowance,
        Integer totalAmount,
        Integer withholdingTax,
        Integer netAmount
) {
    public static PayrollSimpleResponse of(final EstimatedPayrollEntity estimatedPayroll) {
        PayrollAmount amount = estimatedPayroll.getPayrollAmount();
        return PayrollSimpleResponse.builder()
                .key(estimatedPayroll.getPayrollKey())
                .bankCode(estimatedPayroll.getBankCode().getDisplayName())
                .account(estimatedPayroll.getAccount())
                .month(estimatedPayroll.getMonth())
                .withholdingType(estimatedPayroll.getWithholdingType().getLabel())
                .totalTime(amount.getTotalTime())
                .baseAmount(amount.getBaseAmount())
                .weeklyAllowance(amount.getWeeklyAllowance())
                .totalAmount(amount.getTotalAmount())
                .withholdingTax(amount.getWithholdingTax())
                .netAmount(amount.getNetAmount())
                .build();
    }

    public static PayrollSimpleResponse of(final PayrollEntity payroll) {
        PayrollAmount amount = payroll.getPayrollAmount();
        return PayrollSimpleResponse.builder()
                .key("abc123")
                .bankCode(payroll.getDepositBankCode().getDisplayName())
                .account(payroll.getDepositAccount())
                .month(payroll.getMonth())
                .withholdingType(payroll.getWithholdingType().getLabel())
                .totalTime(amount.getTotalTime())
                .baseAmount(amount.getBaseAmount())
                .weeklyAllowance(amount.getWeeklyAllowance())
                .totalAmount(amount.getTotalAmount())
                .withholdingTax(amount.getWithholdingTax())
                .netAmount(amount.getNetAmount())
                .build();
    }
}
