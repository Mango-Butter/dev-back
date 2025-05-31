package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.app.domain.service.payroll.EstimatedPayroll;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayslipEntity;
import lombok.Builder;

@Builder
public record PayrollResponse (
        PayrollDetailResponse data,
        PayrollInfoResponse info
){
    public static PayrollResponse ofForPayroll(final PayrollEntity payroll, final PayslipEntity payslip) {
        return PayrollResponse.builder()
                .data(PayrollDetailResponse.fromEntity(payroll))
                .info(PayrollInfoResponse.of(payroll, payslip))
                .build();
    }
    public static PayrollResponse ofForNoPayroll(final EstimatedPayroll estimatedPayroll) {
        return PayrollResponse.builder()
                .data(PayrollDetailResponse.fromEstimated(estimatedPayroll))
                .build();
    }
}
