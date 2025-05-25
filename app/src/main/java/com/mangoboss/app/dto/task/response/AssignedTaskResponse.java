package com.mangoboss.app.dto.task.response;

import com.mangoboss.storage.task.TaskEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record AssignedTaskResponse(
        Long taskId,
        String title,
        String description,
        LocalDate taskDate,
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean isPhotoRequired,
        String referenceImageUrl,
        TaskLogDetailResponse taskLog
) {
    public static AssignedTaskResponse of(
            final TaskEntity task,
            final TaskLogDetailResponse taskLog
    ) {
        return AssignedTaskResponse.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .taskDate(task.getTaskDate())
                .startTime(task.getStartTime())
                .endTime(task.getEndTime())
                .isPhotoRequired(task.isPhotoRequired())
                .referenceImageUrl(task.getReferenceImageUrl())
                .taskLog(taskLog)
                .build();
    }
}

