package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayslipEntity;
import lombok.Builder;

import java.util.Optional;

@Builder
public record PayrollWithPayslipResponse(
        PayrollDetailResponse payroll,
        Long payslipId
) {
    public static PayrollWithPayslipResponse of(final PayrollEntity payroll, final Optional<PayslipEntity> payslip) {
        if (payslip.isEmpty()) {
            return PayrollWithPayslipResponse.builder()
                    .payroll(PayrollDetailResponse.fromEntity(payroll))
                    .payslipId(null)
                    .build();
        }
        return PayrollWithPayslipResponse.builder()
                .payroll(PayrollDetailResponse.fromEntity(payroll))
                .payslipId(payslip.get().getId())
                .build();
    }
}
