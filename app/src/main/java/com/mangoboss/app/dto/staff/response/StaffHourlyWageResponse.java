package com.mangoboss.app.dto.staff.response;

import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

@Builder
public record StaffHourlyWageResponse (
        StaffSimpleResponse staff,
        Integer hourlyWage
){
    public static StaffHourlyWageResponse fromEntity(
            final StaffEntity staff
    ) {
        return StaffHourlyWageResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staff))
                .hourlyWage(staff.getHourlyWage())
                .build();
    }
}
