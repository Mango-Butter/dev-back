package com.mangoboss.app.dto.attendance.response;

import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import com.mangoboss.storage.schedule.ScheduleEntity;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record AttendanceDetailResponse(
        Long scheduleId,
        LocalDate workDate,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime clockInTime,
        LocalDateTime clockOutTime,
        ClockInStatus clockInStatus,
        ClockOutStatus clockOutStatus
) {
    public static AttendanceDetailResponse fromEntity(final AttendanceEntity attendance) {
        final ScheduleEntity schedule = attendance.getSchedule();

        return AttendanceDetailResponse.builder()
                .scheduleId(schedule.getId())
                .clockInTime(attendance.getClockInTime())
                .clockOutTime(attendance.getClockOutTime())
                .clockInStatus(attendance.getClockInStatus())
                .clockOutStatus(attendance.getClockOutStatus())
                .workDate(schedule.getWorkDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}
