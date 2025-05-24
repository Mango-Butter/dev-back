package com.mangoboss.app.dto.task.request;

import com.mangoboss.storage.task.TaskLogVerificationType;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record MonthlyTaskRoutineCreateRequest(
        @NotBlank
        String title,

        String description,

        @NotNull
        TaskRoutineRepeatType taskRoutineRepeatType,

        @Valid @NotNull
        List<RepeatDateTime> repeatDateTimes,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate,

        @NotNull
        TaskLogVerificationType verificationType,

        String referenceImageFileKey
) implements TaskRoutineBaseRequest {}