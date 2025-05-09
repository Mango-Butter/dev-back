package com.mangoboss.app.dto.dashboard.response;

import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

import java.time.DayOfWeek;
import java.util.List;

@Builder
public record StaffAttendanceSummaryResponse(
        StaffSimpleResponse staff,
        List<DayOfWeek> workDays,
        StaffAttendanceCountResponse attendanceCount
) {
    public static StaffAttendanceSummaryResponse of(
            StaffEntity staffEntity,
            List<DayOfWeek> workDays,
            StaffAttendanceCountResponse staffAttendanceCountResponse
    ) {
        return StaffAttendanceSummaryResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staffEntity))
                .workDays(workDays)
                .attendanceCount(staffAttendanceCountResponse)
                .build();
    }
}