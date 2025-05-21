package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

@Builder
public record PayrollEstimatedResponse (
        StaffSimpleResponse staff,
        PayrollSimpleResponse payroll
){
    public static PayrollEstimatedResponse of(final EstimatedPayrollEntity estimatedPayroll, final StaffEntity staff){
        return PayrollEstimatedResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staff))
                .payroll(PayrollSimpleResponse.of(estimatedPayroll))
                .build();
    }

    public static PayrollEstimatedResponse of(final PayrollEntity payroll, final StaffEntity staff){
        return PayrollEstimatedResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staff))
                .payroll(PayrollSimpleResponse.of(payroll))
                .build();
    }
}
