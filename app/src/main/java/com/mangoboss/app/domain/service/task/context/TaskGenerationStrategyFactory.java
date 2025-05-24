package com.mangoboss.app.domain.service.task.context;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.service.task.strategy.TaskRoutineGenerationStrategy;
import com.mangoboss.storage.task.TaskRoutineRepeatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class TaskGenerationStrategyFactory {

    private final Map<TaskRoutineRepeatType, TaskRoutineGenerationStrategy> strategyMap;

    @Autowired
    public TaskGenerationStrategyFactory(final List<TaskRoutineGenerationStrategy> strategies) {
        this.strategyMap = new EnumMap<>(TaskRoutineRepeatType.class);
        for (TaskRoutineGenerationStrategy strategy : strategies) {
            strategyMap.put(strategy.getType(), strategy);
        }
    }

    public TaskRoutineGenerationStrategy getStrategy(final TaskRoutineRepeatType repeatType) {
        final TaskRoutineGenerationStrategy strategy = strategyMap.get(repeatType);
        if (strategy == null) {
            throw new CustomException(CustomErrorInfo.TASK_ROUTINE_STRATEGY_NOT_FOUND);
        }
        return strategy;
    }
}