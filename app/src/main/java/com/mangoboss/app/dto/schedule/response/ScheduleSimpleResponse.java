package com.mangoboss.app.dto.schedule.response;

import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.schedule.ScheduleState;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ScheduleSimpleResponse(
        Long scheduleId,
        LocalDate workDate,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ScheduleState state
) {
    public static ScheduleSimpleResponse fromEntity(final ScheduleEntity schedule) {
        return ScheduleSimpleResponse.builder()
                .scheduleId(schedule.getId())
                .workDate(schedule.getWorkDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .state(schedule.getState())
                .build();
    }
}
