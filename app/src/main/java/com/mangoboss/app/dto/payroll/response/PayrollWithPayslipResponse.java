package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayslipEntity;
import lombok.Builder;

@Builder
public record PayrollWithPayslipResponse(
        PayrollDataResponse data,
        PayrollInfoResponse info
) {
    public static PayrollWithPayslipResponse of(final PayrollEntity payroll, final PayslipEntity payslip) {
        return PayrollWithPayslipResponse.builder()
                .data(PayrollDataResponse.fromEntity(payroll))
                .info(PayrollInfoResponse.of(payroll, payslip))
                .build();
    }
}
