package com.mangoboss.app.domain.service.task;

import com.mangoboss.app.domain.service.task.strategy.TaskRoutineGenerationStrategy;
import com.mangoboss.app.domain.service.task.context.TaskGenerationStrategyFactory;
import com.mangoboss.app.domain.repository.TaskRepository;
import com.mangoboss.app.domain.repository.TaskRoutineRepository;
import com.mangoboss.app.dto.task.request.SingleTaskCreateRequest;
import com.mangoboss.app.dto.task.request.TaskRoutineBaseRequest;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskRoutineEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRoutineRepository taskRoutineRepository;
    private final TaskRepository taskRepository;
    private final TaskGenerationStrategyFactory strategyFactory;

    public void createTaskRoutineWithTasks(final Long storeId, final TaskRoutineBaseRequest request) {
        final TaskRoutineGenerationStrategy strategy = strategyFactory.getStrategy(request.taskRoutineRepeatType());
        final TaskRoutineEntity routine = strategy.generateTaskRoutine(request, storeId);
        List<TaskEntity> tasks = strategy.generateTasks(routine, storeId);

        taskRoutineRepository.save(routine);
        taskRepository.saveAll(tasks);
    }

    public void createSingleTask(final Long storeId, final SingleTaskCreateRequest request) {
        final TaskEntity task = TaskEntity.create(
                storeId,
                request.taskDate(),
                request.startTime(),
                request.endTime(),
                request.title(),
                request.description(),
                request.verificationType(),
                request.referenceImageFileKey()
        );

        taskRepository.save(task);
    }
}