package com.mangoboss.app.dto.task.request;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;

public record RepeatRule(
        List<@NotNull DayOfWeek> repeatDays,

        List<@NotNull Integer> repeatDates
) {}
