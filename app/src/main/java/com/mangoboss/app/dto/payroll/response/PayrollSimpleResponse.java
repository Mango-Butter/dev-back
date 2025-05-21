package com.mangoboss.app.dto.payroll.response;

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
    public static PayrollSimpleResponse of(final EstimatedPayrollEntity payroll) {
        return PayrollSimpleResponse.builder()
                .key(payroll.getPayrollKey())
                .bankCode(payroll.getBankCode())
                .account(payroll.getAccount())
                .month(payroll.getMonth())
                .withholdingType(payroll.getWithholdingType())
                .totalTime(payroll.getTotalTime())
                .baseAmount(payroll.getBaseAmount())
                .weeklyAllowance(payroll.getWeeklyAllowance())
                .totalAmount(payroll.getTotalAmount())
                .withholdingTax(payroll.getWithholdingTax())
                .netAmount(payroll.getNetAmount())
                .build();
    }
}
