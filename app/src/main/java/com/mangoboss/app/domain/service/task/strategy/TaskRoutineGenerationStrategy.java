package com.mangoboss.app.domain.service.task.strategy;

import com.mangoboss.app.dto.task.request.TaskRoutineBaseRequest;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskRoutineEntity;
import com.mangoboss.storage.task.TaskRoutineRepeatType;

import java.util.List;

public interface TaskRoutineGenerationStrategy {
    TaskRoutineRepeatType getType();

    TaskRoutineEntity generateTaskRoutine(TaskRoutineBaseRequest request, Long storeId);

    List<TaskEntity> generateTasks(TaskRoutineEntity routine, Long storeId);
}