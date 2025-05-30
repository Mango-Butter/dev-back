package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.storage.payroll.PayrollAmount;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.TransferState;
import lombok.Builder;

@Builder
public record PayrollSimpleResponse(
        Long payrollId,
        String bankCode,
        String account,
        Double totalTime,
        Integer netAmount,
        String transferState
) {
    public static PayrollSimpleResponse fromEntity(final PayrollEntity payroll) {
        PayrollAmount payrollAmount = payroll.getPayrollAmount();
        return PayrollSimpleResponse.builder()
                .payrollId(payroll.getId())
                .bankCode(payroll.getDepositBankCode().getDisplayName())
                .account(payroll.getDepositAccount())
                .totalTime(payrollAmount.getTotalTime())
                .netAmount(payrollAmount.getNetAmount())
                .transferState(getStateFor(payroll.getTransferState()))
                .build();
    }

    private static String getStateFor(final TransferState transferState) {
        return switch (transferState) {
            case PENDING -> "PENDING";
            case COMPLETED_TRANSFERRED -> "COMPLETED";
            default -> "FAILED";
        };
    }
}
