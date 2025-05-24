package com.mangoboss.app.dto.task.request;

import com.mangoboss.storage.task.TaskLogVerificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record SingleTaskCreateRequest(
        @NotBlank String title,
        String description,

        @NotNull LocalDate taskDate,

        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,

        @NotNull TaskLogVerificationType verificationType,

        String referenceImageFileKey
) {}