package com.mangoboss.app.dto.task.request;

import com.mangoboss.storage.task.TaskEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record SingleTaskCreateRequest(
        @NotBlank
        String title,

        @NotBlank
        String description,

        @NotNull
        LocalDate taskDate,

        @NotNull
        LocalDateTime startTime,

        @NotNull
        LocalDateTime endTime,

        @NotNull
        boolean photoRequired,

        String referenceImageUrl
) {
        public TaskEntity toEntity(final Long storeId) {
                return TaskEntity.create(
                        storeId,
                        taskDate,
                        startTime,
                        endTime,
                        title,
                        description,
                        photoRequired,
                        referenceImageUrl
                );
        }
}