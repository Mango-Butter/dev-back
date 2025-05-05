package com.mangoboss.app.dto.schedule.request;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record ScheduleUpdateRequest(
        @NonNull
        LocalDate workDate,

        @NonNull
        LocalTime startTime,

        @NonNull
        LocalTime endTime
) {
        public LocalDateTime toStartDateTime(){
                return LocalDateTime.of(workDate, startTime);
        }

        public LocalDateTime toEndDateTime(){
                return LocalDateTime.of(workDate, endTime);
        }
}
