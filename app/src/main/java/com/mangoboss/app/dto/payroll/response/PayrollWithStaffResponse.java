package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

@Builder
public record PayrollWithStaffResponse(
        StaffSimpleResponse staff,
        PayrollSimpleResponse simplePayroll
) {
    public static PayrollWithStaffResponse of(final PayrollEntity payroll, final StaffEntity staff) {
        return PayrollWithStaffResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staff))
                .simplePayroll(PayrollSimpleResponse.fromEntity(payroll))
                .build();
    }
}
