package com.mangoboss.app.dto.dashboard.response;

import com.mangoboss.storage.attendance.projection.StaffAttendanceCountProjection;
import lombok.Builder;

@Builder
public record StaffAttendanceCountResponse(
        Integer normalCount,
        Integer lateCount,
        Integer absentCount
) {
    public static StaffAttendanceCountResponse of(final StaffAttendanceCountProjection projection) {
        return StaffAttendanceCountResponse.builder()
                .normalCount(projection.getNormalCount())
                .lateCount(projection.getLateCount())
                .absentCount(projection.getAbsentCount())
                .build();
    }
}