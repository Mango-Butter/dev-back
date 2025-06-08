package com.mangoboss.app.dto.schedule.request;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleUpdateRequest(
        @NonNull
        LocalDate workDate,

        @NonNull
        LocalTime startTime,

        @NonNull
        LocalTime endTime
) {
}
