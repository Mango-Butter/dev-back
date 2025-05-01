package com.mangoboss.app.dto.attendance.response;

import com.mangoboss.storage.attendance.AttendanceEntity;
import lombok.Builder;

@Builder
public record ClockInResponse(
        Long attendanceId
) {
    public static ClockInResponse fromEntity(final AttendanceEntity attendance) {
        return ClockInResponse.builder()
                .attendanceId(attendance.getId())
                .build();
    }
}