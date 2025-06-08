package com.mangoboss.app.domain.service.task.strategy;

import com.mangoboss.app.common.validator.RepeatRuleValidator;
import com.mangoboss.app.dto.task.request.TaskRoutineCreateRequest;
import com.mangoboss.storage.task.*;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
public class MonthlyTaskRoutineGenerationStrategy implements TaskRoutineGenerationStrategy {

    @Override
    public TaskRoutineRepeatType getType() {
        return TaskRoutineRepeatType.MONTHLY;
    }

    @Override
    public TaskRoutineEntity generateTaskRoutine(final TaskRoutineCreateRequest request, final Long storeId) {
        RepeatRuleValidator.validateMonthlyRepeatDates(request.repeatRule());

        return TaskRoutineEntity.createMonthly(
                storeId,
                request.title(),
                request.description(),
                request.repeatRule().repeatDates(),
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
                .filter(date -> routine.getRepeatDates().contains(date.getDayOfMonth()))
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