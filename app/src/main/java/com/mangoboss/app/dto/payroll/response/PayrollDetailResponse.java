package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.app.domain.service.payroll.EstimatedPayroll;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.WithholdingType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PayrollDetailResponse(
        String staffName,
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
){
    public static PayrollDetailResponse fromEntity(final PayrollEntity payroll) {
        return PayrollDetailResponse.builder()
                .staffName(payroll.getStaffName())
                .bankCode(payroll.getDepositBankCode().getDisplayName())
                .account(payroll.getDepositAccount())
                .month(payroll.getMonth())
                .withholdingType(payroll.getWithholdingType())
                .totalTime(payroll.getPayrollAmount().getTotalTime())
                .baseAmount(payroll.getPayrollAmount().getBaseAmount())
                .weeklyAllowance(payroll.getPayrollAmount().getWeeklyAllowance())
                .totalCommutingAllowance(payroll.getPayrollAmount().getTotalCommutingAllowance())
                .totalAmount(payroll.getPayrollAmount().getTotalAmount())
                .withholdingTax(payroll.getPayrollAmount().getWithholdingTax())
                .netAmount(payroll.getPayrollAmount().getNetAmount())
                .build();
    }

    public static PayrollDetailResponse fromEstimated(final EstimatedPayroll estimatedPayroll) {
        if(estimatedPayroll.getBankCode() == null || estimatedPayroll.getAccount() == null){
            return PayrollDetailResponse.builder()
                    .staffName(estimatedPayroll.getStaffName())
                    .bankCode(null)
                    .account(null)
                    .month(estimatedPayroll.getMonth())
                    .withholdingType(estimatedPayroll.getWithholdingType())
                    .totalTime(estimatedPayroll.getTotalTime())
                    .baseAmount(estimatedPayroll.getBaseAmount())
                    .weeklyAllowance(estimatedPayroll.getWeeklyAllowance())
                    .totalCommutingAllowance(estimatedPayroll.getTotalCommutingAllowance())
                    .totalAmount(estimatedPayroll.getTotalAmount())
                    .withholdingTax(estimatedPayroll.getWithholdingTax())
                    .netAmount(estimatedPayroll.getNetAmount())
                    .build();
        }
        return PayrollDetailResponse.builder()
                .staffName(estimatedPayroll.getStaffName())
                .bankCode(estimatedPayroll.getBankCode().getDisplayName())
                .account(estimatedPayroll.getAccount())
                .month(estimatedPayroll.getMonth())
                .withholdingType(estimatedPayroll.getWithholdingType())
                .totalTime(estimatedPayroll.getTotalTime())
                .baseAmount(estimatedPayroll.getBaseAmount())
                .weeklyAllowance(estimatedPayroll.getWeeklyAllowance())
                .totalCommutingAllowance(estimatedPayroll.getTotalCommutingAllowance())
                .totalAmount(estimatedPayroll.getTotalAmount())
                .withholdingTax(estimatedPayroll.getWithholdingTax())
                .netAmount(estimatedPayroll.getNetAmount())
                .build();
    }
}
