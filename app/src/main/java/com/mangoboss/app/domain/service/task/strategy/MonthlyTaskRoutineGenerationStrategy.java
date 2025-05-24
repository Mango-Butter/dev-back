package com.mangoboss.app.domain.service.task.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mangoboss.app.common.util.JsonConverter;
import com.mangoboss.app.dto.task.request.MonthlyTaskRoutineCreateRequest;
import com.mangoboss.app.dto.task.request.RepeatDateTime;
import com.mangoboss.storage.task.*;
import com.mangoboss.app.dto.task.request.TaskRoutineBaseRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonthlyTaskRoutineGenerationStrategy implements TaskRoutineGenerationStrategy {

    @Override
    public TaskRoutineRepeatType getType() {
        return TaskRoutineRepeatType.MONTHLY;
    }

    @Override
    public TaskRoutineEntity generateTaskRoutine(final TaskRoutineBaseRequest request, final Long storeId) {
        final MonthlyTaskRoutineCreateRequest r = (MonthlyTaskRoutineCreateRequest) request;
        return TaskRoutineEntity.createMonthly(
                storeId,
                r.title(),
                r.description(),
                JsonConverter.toJson(r.repeatDateTimes()),
                r.startDate(),
                r.endDate(),
                r.verificationType(),
                r.referenceImageFileKey()
        );
    }

    @Override
    public List<TaskEntity> generateTasks(final TaskRoutineEntity routine, final Long storeId) {
        final List<TaskEntity> tasks = new ArrayList<>();
        final List<RepeatDateTime> repeatDateTimes = JsonConverter.fromJson(
                routine.getRepeatDates(),
                new TypeReference<>() {}
        );

        for (RepeatDateTime dateTime : repeatDateTimes) {
            for (LocalDate date = routine.getStartDate(); !date.isAfter(routine.getEndDate()); date = date.plusDays(1)) {
                if (date.getDayOfMonth() == dateTime.date()) {
                    tasks.add(TaskEntity.createFromTaskRoutine(routine, storeId, date, dateTime.startTime(), dateTime.endTime()));
                }
            }
        }

        return tasks;
    }
}