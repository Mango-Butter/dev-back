package com.mangoboss.app.dto.schedule.response;

import com.mangoboss.storage.schedule.ScheduleEntity;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ScheduleSimpleResponse(
        Long scheduleId,
        LocalDate workDate,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static ScheduleSimpleResponse fromEntity(final ScheduleEntity schedule) {
        return ScheduleSimpleResponse.builder()
                .scheduleId(schedule.getId())
                .workDate(schedule.getWorkDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}
