package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.app.domain.service.payroll.EstimatedPayroll;
import lombok.Builder;

@Builder
public record PayrollEstimatedResponse(
        String key,
        PayrollDataResponse data
) {
    public static PayrollEstimatedResponse of(final EstimatedPayroll estimatedPayroll) {
        if (estimatedPayroll.getAccount() == null || estimatedPayroll.getBankCode() == null) {
            return PayrollEstimatedResponse.builder()
                    .key(null)
                    .data(PayrollDataResponse.fromEstimated(estimatedPayroll))
                    .build();
        }
        return PayrollEstimatedResponse.builder()
                .key(estimatedPayroll.getKey())
                .data(PayrollDataResponse.fromEstimated(estimatedPayroll))
                .build();
    }
}
