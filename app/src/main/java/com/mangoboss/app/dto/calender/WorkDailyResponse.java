package com.mangoboss.app.dto.calender;

import com.mangoboss.app.dto.attendance.response.AttendanceSimpleResponse;
import com.mangoboss.app.dto.schedule.response.ScheduleSimpleResponse;
import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.schedule.ScheduleEntity;
import lombok.Builder;

@Builder
public record WorkDailyResponse(
        StaffSimpleResponse staff,
        ScheduleSimpleResponse schedule,
        AttendanceSimpleResponse attendance
) {
    public static WorkDailyResponse of(final ScheduleEntity schedule) {
        if(schedule.getAttendance() == null){
            return WorkDailyResponse.builder()
                    .staff(StaffSimpleResponse.fromEntity(schedule.getStaff()))
                    .schedule(ScheduleSimpleResponse.fromEntity(schedule))
                    .build();
        }
        return WorkDailyResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(schedule.getStaff()))
                .schedule(ScheduleSimpleResponse.fromEntity(schedule))
                .attendance(AttendanceSimpleResponse.fromEntity(schedule.getAttendance()))
                .build();
    }
}
