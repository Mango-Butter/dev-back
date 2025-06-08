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
public class WeeklyTaskRoutineGenerationStrategy implements TaskRoutineGenerationStrategy {

    @Override
    public TaskRoutineRepeatType getType() {
        return TaskRoutineRepeatType.WEEKLY;
    }

    @Override
    public TaskRoutineEntity generateTaskRoutine(final TaskRoutineCreateRequest request, final Long storeId) {
        RepeatRuleValidator.validateWeeklyRepeatDays(request.repeatRule());

        return TaskRoutineEntity.createWeekly(
                storeId,
                request.title(),
                request.description(),
                request.repeatRule().repeatDays(),
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
                .filter(date -> routine.getRepeatDays().contains(date.getDayOfWeek()))
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