package com.mangoboss.app.domain.service.task.context;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.service.task.strategy.TaskRoutineGenerationStrategy;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskRoutineStrategyContext {

    private final List<TaskRoutineGenerationStrategy> strategies;

    public TaskRoutineGenerationStrategy findStrategy(TaskRoutineRepeatType repeatType) {
        return strategies.stream()
                .filter(s -> s.getType() == repeatType)
                .findFirst()
                .orElseThrow(() -> new CustomException(CustomErrorInfo.TASK_ROUTINE_STRATEGY_NOT_FOUND));
    }
}