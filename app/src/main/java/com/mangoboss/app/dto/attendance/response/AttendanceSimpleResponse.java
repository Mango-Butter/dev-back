package com.mangoboss.app.dto.attendance.response;

import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.AttendanceState;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AttendanceSimpleResponse(
        LocalDateTime clockInTime,
        LocalDateTime clockOutTime,
        ClockInStatus clockInStatus,
        ClockOutStatus clockOutStatus,
        AttendanceState attendanceState
) {
    public static AttendanceSimpleResponse fromEntity(final AttendanceEntity attendance) {
        return AttendanceSimpleResponse.builder()
                .clockInTime(attendance.getClockInTime())
                .clockOutTime(attendance.getClockOutTime())
                .clockInStatus(attendance.getClockInStatus())
                .clockOutStatus(attendance.getClockOutStatus())
                .attendanceState(attendance.getAttendanceState())
                .build();
    }
}
