package com.mangoboss.app.dto.staff.request;

import com.mangoboss.storage.payroll.WithholdingType;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

@Builder
public record StaffInfoResponse (
        Integer hourlyWage,
        WithholdingType withholdingType
){
    public static StaffInfoResponse fromEntity(final StaffEntity staff) {
        return StaffInfoResponse.builder()
                .hourlyWage(staff.getHourlyWage())
                .withholdingType(staff.getWithholdingType())
                .build();
    }
}
