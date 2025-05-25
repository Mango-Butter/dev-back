package com.mangoboss.app.dto.task.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record TaskRoutineCreateRequest(
        @NotNull
        TaskRoutineRepeatType taskRoutineRepeatType,

        @NotBlank
        String title,

        String description,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate,

        @JsonFormat(pattern = "HH:mm")
        @NotNull
        LocalTime startTime,

        @JsonFormat(pattern = "HH:mm")
        @NotNull
        LocalTime endTime,

        @NotNull
        boolean photoRequired,

        String referenceImageUrl,

        @Valid
        RepeatRule repeatRule
) { }