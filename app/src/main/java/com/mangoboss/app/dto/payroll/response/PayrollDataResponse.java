package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.app.domain.service.payroll.EstimatedPayroll;
import com.mangoboss.storage.payroll.PayrollEntity;
import lombok.Builder;

@Builder
public record PayrollDataResponse(
        String bankCode,
        String account,
        Double totalTime,
        Integer netAmount
){
    public static PayrollDataResponse fromEntity(final PayrollEntity payroll) {
        return PayrollDataResponse.builder()
                .bankCode(payroll.getDepositBankCode().getDisplayName())
                .account(payroll.getDepositAccount())
                .totalTime(payroll.getPayrollAmount().getTotalTime())
                .netAmount(payroll.getPayrollAmount().getNetAmount())
                .build();
    }

    public static PayrollDataResponse fromEstimated(final EstimatedPayroll estimatedPayroll) {
        if(estimatedPayroll.getBankCode() == null || estimatedPayroll.getAccount() == null){
            return PayrollDataResponse.builder()
                    .bankCode(null)
                    .account(null)
                    .totalTime(estimatedPayroll.getTotalTime())
                    .netAmount(estimatedPayroll.getNetAmount())
                    .build();
        }
        return PayrollDataResponse.builder()
                .bankCode(estimatedPayroll.getBankCode().getDisplayName())
                .account(estimatedPayroll.getAccount())
                .totalTime(estimatedPayroll.getTotalTime())
                .netAmount(estimatedPayroll.getNetAmount())
                .build();
    }
}