package com.mangoboss.app.domain.service.task.strategy;

import com.mangoboss.app.common.validator.RepeatRuleValidator;
import com.mangoboss.app.dto.task.request.TaskRoutineCreateRequest;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskRoutineEntity;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class DailyTaskRoutineGenerationStrategy implements TaskRoutineGenerationStrategy {

    @Override
    public TaskRoutineRepeatType getType() {
        return TaskRoutineRepeatType.DAILY;
    }

    @Override
    public TaskRoutineEntity generateTaskRoutine(final TaskRoutineCreateRequest request, final Long storeId) {
        RepeatRuleValidator.validateNoRepeatRuleForDaily(request.repeatRule());
        return TaskRoutineEntity.createDaily(
                storeId,
                request.title(),
                request.description(),
                request.startDate(),
                request.endDate(),
                request.startTime(),
                request.endTime(),
                request.photoRequired(),
                request.referenceImageUrl()
        );
    }

    @Override
    public List<TaskEntity> generateTasks(final TaskRoutineEntity routine, final Long storeId) {
        return Stream.iterate(routine.getStartDate(), date -> !date.isAfter(routine.getEndDate()), date -> date.plusDays(1))
                .map(date -> TaskEntity.createFromTaskRoutine(
                        routine,
                        storeId,
                        date,
                        date.atTime(routine.getStartTime()),
                        date.atTime(routine.getEndTime())
                ))
                .toList();
    }
}