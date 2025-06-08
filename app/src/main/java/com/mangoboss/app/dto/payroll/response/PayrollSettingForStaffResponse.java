package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.app.dto.payroll.DeductionUnit;
import com.mangoboss.storage.payroll.PayrollSettingEntity;
import lombok.Builder;

@Builder
public record PayrollSettingForStaffResponse (
        Boolean autoTransferEnabled,
        Integer transferDate,
        DeductionUnit deductionUnit,
        Integer commutingAllowance
){
    public static PayrollSettingForStaffResponse fromEntity(final PayrollSettingEntity payrollSetting) {
        return PayrollSettingForStaffResponse.builder()
                .autoTransferEnabled(payrollSetting.getAutoTransferEnabled())
                .transferDate(payrollSetting.getTransferDate())
                .deductionUnit(DeductionUnit.getDeductionUnit(payrollSetting.getDeductionUnit()))
                .commutingAllowance(payrollSetting.getCommutingAllowance())
                .build();
    }
}
