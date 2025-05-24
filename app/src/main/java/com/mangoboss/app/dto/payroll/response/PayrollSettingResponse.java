package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.app.dto.payroll.DeductionUnit;
import com.mangoboss.storage.payroll.PayrollSettingEntity;
import com.mangoboss.storage.payroll.TransferAccountEntity;
import lombok.Builder;

@Builder
public record PayrollSettingResponse(
        AccountResponse account,
        Boolean autoTransferEnabled,
        Integer transferDate,
        DeductionUnit deductionUnit
) {
    public static PayrollSettingResponse fromEntity(final PayrollSettingEntity payrollSetting) {
        final TransferAccountEntity transferAccount = payrollSetting.getTransferAccountEntity();
        if (transferAccount == null) {
            return PayrollSettingResponse.builder()
                    .autoTransferEnabled(payrollSetting.getAutoTransferEnabled())
                    .transferDate(payrollSetting.getTransferDate())
                    .deductionUnit(DeductionUnit.getDeductionUnit(payrollSetting.getDeductionUnit()))
                    .build();
        }
        return PayrollSettingResponse.builder()
                .account(AccountResponse.fromEntity(transferAccount))
                .autoTransferEnabled(payrollSetting.getAutoTransferEnabled())
                .transferDate(payrollSetting.getTransferDate())
                .deductionUnit(DeductionUnit.getDeductionUnit(payrollSetting.getDeductionUnit()))
                .build();
    }
}
