package com.mangoboss.app.domain.service.task.strategy;

import com.mangoboss.app.dto.task.request.DailyTaskRoutineCreateRequest;
import com.mangoboss.app.dto.task.request.TaskRoutineBaseRequest;
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
public class DailyTaskRoutineGenerationStrategy implements TaskRoutineGenerationStrategy {

    @Override
    public TaskRoutineRepeatType getType() {
        return TaskRoutineRepeatType.DAILY;
    }

    @Override
    public TaskRoutineEntity generateTaskRoutine(final TaskRoutineBaseRequest request, final Long storeId) {
        final DailyTaskRoutineCreateRequest dailyTaskRoutineCreateRequest = (DailyTaskRoutineCreateRequest) request;
        return TaskRoutineEntity.createDaily(
                storeId,
                dailyTaskRoutineCreateRequest.title(),
                dailyTaskRoutineCreateRequest.description(),
                dailyTaskRoutineCreateRequest.startDate(),
                dailyTaskRoutineCreateRequest.endDate(),
                dailyTaskRoutineCreateRequest.startTime(),
                dailyTaskRoutineCreateRequest.endTime(),
                dailyTaskRoutineCreateRequest.verificationType(),
                dailyTaskRoutineCreateRequest.referenceImageFileKey()
        );
    }

    @Override
    public List<TaskEntity> generateTasks(final TaskRoutineEntity routine, final Long storeId) {
        final List<TaskEntity> tasks = new ArrayList<>();
        LocalDate current = routine.getStartDate();

        while (!current.isAfter(routine.getEndDate())) {
            tasks.add(TaskEntity.createFromTaskRoutine(routine, storeId, current, routine.getStartTime(), routine.getEndTime()));
            current = current.plusDays(1);
        }

        return tasks;
    }
}