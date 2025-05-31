package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.TransferState;
import com.mangoboss.storage.payroll.WithholdingType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PayrollDetailResponse (
        Long payrollId,
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
        Integer netAmount,
        String transferState
){
    public static PayrollDetailResponse fromEntity(final PayrollEntity payroll){
        return PayrollDetailResponse.builder()
                .payrollId(payroll.getId())
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
