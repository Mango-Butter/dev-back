package com.mangoboss.app.dto.calender;

import com.mangoboss.app.dto.attendance.response.AttendanceSimpleResponse;
import com.mangoboss.app.dto.schedule.response.ScheduleSimpleResponse;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.schedule.ScheduleEntity;
import lombok.Builder;

@Builder
public record WorkResponse(
        ScheduleSimpleResponse schedule,
        AttendanceSimpleResponse attendance
) {
    public static WorkResponse of(final ScheduleEntity schedule) {
        final AttendanceEntity attendance = schedule.getAttendance();
        if (attendance == null) {
            return WorkResponse.builder()
                    .schedule(ScheduleSimpleResponse.fromEntity(schedule))
                    .attendance(null)
                    .build();
        }
        return WorkResponse.builder()
                .schedule(ScheduleSimpleResponse.fromEntity(schedule))
                .attendance(AttendanceSimpleResponse.fromEntity(attendance))
                .build();
    }
}
