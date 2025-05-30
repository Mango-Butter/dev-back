package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.estimated.EstimatedPayrollEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

@Builder
public record PayrollEstimatedWithStaffResponse(
        StaffSimpleResponse staff,
        PayrollEstimatedResponse payroll
){
    public static PayrollEstimatedWithStaffResponse of(final EstimatedPayrollEntity estimatedPayroll, final StaffEntity staff){
        return PayrollEstimatedWithStaffResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staff))
                .payroll(PayrollEstimatedResponse.of(estimatedPayroll))
                .build();
    }
}
