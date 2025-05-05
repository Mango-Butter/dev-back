package com.mangoboss.app.dto.schedule.response;

import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.schedule.ScheduleEntity;
import lombok.Builder;

// todo 삭제해야 함
@Builder
public record ScheduleDailyResponse (
        StaffSimpleResponse staff,
        ScheduleSimpleResponse schedule
) {
    public static ScheduleDailyResponse fromEntity(final ScheduleEntity schedule) {
        return ScheduleDailyResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(schedule.getStaff()))
                .schedule(ScheduleSimpleResponse.fromEntity(schedule))
                .build();
    }
}
