package com.mangoboss.app.dto.payroll.request;

import com.mangoboss.app.dto.payroll.DeductionUnit;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record PayrollSettingRequest(
        @NonNull
        Boolean autoTransferEnabled,

        @Min(1)
        @Max(28)
        Integer transferDate,

        Integer imit,

        @NonNull
        DeductionUnit deductionUnit
) {
}
