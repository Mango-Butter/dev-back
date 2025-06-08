package com.mangoboss.app.dto.attendance.response;

import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AttendanceSimpleForEditResponse(
        LocalDateTime clockInTime,
        LocalDateTime clockOutTime,
        ClockInStatus clockInStatus
) {
    public static AttendanceSimpleForEditResponse ofOriginal(final AttendanceEditEntity attendanceEdit) {
        return AttendanceSimpleForEditResponse.builder()
                .clockInTime(attendanceEdit.getOriginalClockInTime())
                .clockOutTime(attendanceEdit.getOriginalClockOutTime())
                .clockInStatus(attendanceEdit.getOriginalClockInStatus())
                .build();
    }

    public static AttendanceSimpleForEditResponse ofRequested(final AttendanceEditEntity attendanceEdit) {
        return AttendanceSimpleForEditResponse.builder()
                .clockInTime(attendanceEdit.getRequestedClockInTime())
                .clockOutTime(attendanceEdit.getRequestedClockOutTime())
                .clockInStatus(attendanceEdit.getRequestedClockInStatus())
                .build();
    }
}