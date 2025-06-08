package com.mangoboss.app.dto.staff.response;

import com.mangoboss.storage.payroll.WithholdingType;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

@Builder
public record StaffWithholdingTypeResponse (
        StaffSimpleResponse staff,
        WithholdingType withholdingType
){
    public static StaffWithholdingTypeResponse fromEntity(
            final StaffEntity staff
    ) {
        return StaffWithholdingTypeResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staff))
                .withholdingType(staff.getWithholdingType())
                .build();
    }
}
