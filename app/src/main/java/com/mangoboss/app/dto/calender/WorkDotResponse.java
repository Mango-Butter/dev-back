package com.mangoboss.app.dto.calender;

import com.mangoboss.storage.attendance.projection.WorkDotProjection;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record WorkDotResponse(
        LocalDate date,
        Integer normalCount,
        Integer lateCount,
        Integer absentCount,
        Integer totalScheduleCount
) {
    public static WorkDotResponse of(final WorkDotProjection workDotProjection) {
        return WorkDotResponse.builder()
                .date(workDotProjection.getDate())
                .normalCount(workDotProjection.getNormalCount())
                .lateCount(workDotProjection.getLateCount())
                .absentCount(workDotProjection.getAbsentCount())
                .totalScheduleCount(workDotProjection.getTotalScheduleCount())
                .build();
    }
}
