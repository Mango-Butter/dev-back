package com.mangoboss.app.dto.dashboard.response;

import com.mangoboss.storage.attendance.projection.StaffAttendanceCountProjection;
import lombok.Builder;

@Builder
public record StaffAttendanceCountResponse(
        int normalCount,
        int lateCount,
        int absentCount
) {
    public static StaffAttendanceCountResponse of(final StaffAttendanceCountProjection projection) {
        return StaffAttendanceCountResponse.builder()
                .normalCount(projection.getNormalCount())
                .lateCount(projection.getLateCount())
                .absentCount(projection.getAbsentCount())
                .build();
    }
}