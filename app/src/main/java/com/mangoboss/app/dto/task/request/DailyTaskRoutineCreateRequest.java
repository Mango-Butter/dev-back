package com.mangoboss.app.dto.task.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mangoboss.storage.task.TaskLogVerificationType;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record DailyTaskRoutineCreateRequest(
        @NotBlank
        String title,

        String description,

        @NotNull
        TaskRoutineRepeatType taskRoutineRepeatType,

        @JsonFormat(pattern = "HH:mm")
        @NotNull LocalTime startTime,

        @JsonFormat(pattern = "HH:mm")
        @NotNull
        LocalTime endTime,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate,

        @NotNull
        TaskLogVerificationType verificationType,

        String referenceImageFileKey
) implements TaskRoutineBaseRequest {}
