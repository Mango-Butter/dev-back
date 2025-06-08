package com.mangoboss.app.dto.contract.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record WorkSchedule(
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        @NotNull
        DayOfWeek dayOfWeek,

        @JsonFormat(pattern = "HH:mm")
        @NotNull
        LocalTime startTime,

        @JsonFormat(pattern = "HH:mm")
        @NotNull
        LocalTime endTime
) {}