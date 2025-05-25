package com.mangoboss.app.domain.service.task;

import com.mangoboss.app.domain.repository.TaskRepository;
import com.mangoboss.app.domain.repository.TaskRoutineRepository;
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

    public void saveTaskRoutineWithTasks(final TaskRoutineEntity taskRoutine, final List<TaskEntity> tasks) {
        taskRoutineRepository.save(taskRoutine);
        taskRepository.saveAll(tasks);
    }

    public void saveSingleTask(final TaskEntity task) {
        taskRepository.save(task);
    }
}