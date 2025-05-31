package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.app.domain.service.payroll.EstimatedPayroll;
import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

@Builder
public record PayrollWithStaffResponse(
        StaffSimpleResponse staff,
        PayrollDataResponse data,
        PayrollInfoResponse info
){
    public static PayrollWithStaffResponse ofForPayroll(final StaffEntity staff, final PayrollEntity payroll, final PayslipEntity payslip) {
        return PayrollWithStaffResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staff))
                .data(PayrollDataResponse.fromEntity(payroll))
                .info(PayrollInfoResponse.of(payroll, payslip))
                .build();
    }

    public static PayrollWithStaffResponse ofForNoPayroll(final StaffEntity staff, final EstimatedPayroll estimatedPayroll) {
        return PayrollWithStaffResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staff))
                .data(PayrollDataResponse.fromEstimated(estimatedPayroll))
                .build();
    }
}
