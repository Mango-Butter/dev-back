package com.mangoboss.app.domain.service.task;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.TaskLogRepository;
import com.mangoboss.app.domain.repository.TaskRepository;
import com.mangoboss.app.domain.repository.TaskRoutineRepository;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskLogEntity;
import com.mangoboss.storage.task.TaskRoutineEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRoutineRepository taskRoutineRepository;
    private final TaskRepository taskRepository;
    private final TaskLogRepository taskLogRepository;

    @Transactional
    public void saveTaskRoutineWithTasks(final TaskRoutineEntity taskRoutine, final List<TaskEntity> tasks) {
        taskRoutineRepository.save(taskRoutine);
        taskRepository.saveAll(tasks);
    }

    @Transactional
    public void saveSingleTask(final TaskEntity task) {
        taskRepository.save(task);
    }

    @Transactional
    public void completeTask(final Long storeId, final Long taskId, final Long staffId, final String taskLogImageUrl) {
        final TaskEntity task = taskRepository.getTaskByIdAndStoreId(taskId, storeId);
        validateNotAlreadyCompleted(task.getId(), staffId);
        validateTaskLogImageRequirement(task, taskLogImageUrl);

        final TaskLogEntity taskLog = TaskLogEntity.create(task, staffId, taskLogImageUrl);

        taskLogRepository.save(taskLog);
    }

    public void validateTaskLogImageRequirement(final TaskEntity task, final String taskLogImageUrl) {
        if (task.isPhotoRequired() && (taskLogImageUrl == null || taskLogImageUrl.isBlank())) {
            throw new CustomException(CustomErrorInfo.TASK_LOG_IMAGE_REQUIRED);
        }
    }

    public void validateNotAlreadyCompleted(final Long taskId, final Long staffId) {
        final boolean alreadyCompleted = taskLogRepository.findByTaskIdAndStaffId(taskId, staffId).isPresent();
        if (alreadyCompleted) {
            throw new CustomException(CustomErrorInfo.ALREADY_COMPLETED_TASK);
        }
    }
}