package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.payroll.TransferState;
import lombok.Builder;

@Builder
public record PayrollInfoResponse(
        Long payrollId,
        TransferStateForResponse transferState,
        Long payslipId
) {
    public static PayrollInfoResponse of(final PayrollEntity payroll, final PayslipEntity payslip) {
        TransferStateForResponse transferState = getStatusForResponse(payroll.getTransferState());
        if (payslip == null) {
            return PayrollInfoResponse.builder()
                    .payrollId(payroll.getId())
                    .transferState(transferState)
                    .payslipId(null)
                    .build();
        }
        return PayrollInfoResponse.builder()
                .payrollId(payroll.getId())
                .transferState(transferState)
                .payslipId(payslip.getId())
                .build();
    }

    private static TransferStateForResponse getStatusForResponse(final TransferState transferState) {
        return switch (transferState) {
            case PENDING -> TransferStateForResponse.PENDING;
            case COMPLETED_TRANSFERRED -> TransferStateForResponse.COMPLETED;
            default -> TransferStateForResponse.FAILED;
        };
    }
}
