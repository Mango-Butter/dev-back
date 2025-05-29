package com.mangoboss.app.dto.task.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mangoboss.storage.task.TaskRoutineEntity;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder
public record TaskRoutineResponse(
        Long id,
        String title,
        String description,
        TaskRoutineRepeatType repeatType,
        List<String> repeatDays,
        List<Integer> repeatDates,
        LocalDate startDate,
        LocalDate endDate,
        @JsonFormat(pattern = "HH:mm") LocalTime startTime,
        @JsonFormat(pattern = "HH:mm") LocalTime endTime,
        boolean photoRequired,
        String referenceImageUrl
) {

    public static TaskRoutineResponse fromEntity(final TaskRoutineEntity entity) {
        return TaskRoutineResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .repeatType(entity.getRepeatType())
                .repeatDays(entity.getRepeatDays() != null
                        ? entity.getRepeatDays().stream().map(DayOfWeek::name).toList()
                        : null)
                .repeatDates(entity.getRepeatDates())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .photoRequired(entity.isPhotoRequired())
                .referenceImageUrl(entity.getReferenceImageUrl())
                .build();
    }
}
