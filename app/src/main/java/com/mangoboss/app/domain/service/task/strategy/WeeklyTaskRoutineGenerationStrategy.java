package com.mangoboss.app.domain.service.task.strategy;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mangoboss.app.common.util.JsonConverter;
import com.mangoboss.app.dto.task.request.RepeatDayTime;
import com.mangoboss.app.dto.task.request.TaskRoutineBaseRequest;
import com.mangoboss.app.dto.task.request.WeeklyTaskRoutineCreateRequest;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskRoutineEntity;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WeeklyTaskRoutineGenerationStrategy implements TaskRoutineGenerationStrategy {

    @Override
    public TaskRoutineRepeatType getType() {
        return TaskRoutineRepeatType.WEEKLY;
    }

    @Override
    public TaskRoutineEntity generateTaskRoutine(final TaskRoutineBaseRequest request, final Long storeId) {
        final WeeklyTaskRoutineCreateRequest weeklyTaskRoutineCreateRequest = (WeeklyTaskRoutineCreateRequest) request;
        return TaskRoutineEntity.createWeekly(
                storeId,
                weeklyTaskRoutineCreateRequest.title(),
                weeklyTaskRoutineCreateRequest.description(),
                JsonConverter.toJson(weeklyTaskRoutineCreateRequest.repeatDayTimes()),
                weeklyTaskRoutineCreateRequest.startDate(),
                weeklyTaskRoutineCreateRequest.endDate(),
                weeklyTaskRoutineCreateRequest.verificationType(),
                weeklyTaskRoutineCreateRequest.referenceImageFileKey()
        );
    }

    @Override
    public List<TaskEntity> generateTasks(TaskRoutineEntity routine, Long storeId) {
        final List<TaskEntity> tasks = new ArrayList<>();
        final List<RepeatDayTime> repeatDayTimes = JsonConverter.fromJson(
                routine.getRepeatDays(),
                new TypeReference<>() {}
        );

        LocalDate date = routine.getStartDate();
        while (!date.isAfter(routine.getEndDate())) {
            for (RepeatDayTime repeat : repeatDayTimes) {
                if (date.getDayOfWeek() == repeat.dayOfWeek()) {
                    tasks.add(TaskEntity.createFromTaskRoutine(routine, storeId, date, repeat.startTime(), repeat.endTime()));
                }
            }
            date = date.plusDays(1);
        }

        return tasks;
    }
}