package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.TaskLogRepository;
import com.mangoboss.storage.task.TaskLogEntity;
import com.mangoboss.storage.task.TaskLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class TaskLogRepositoryImpl implements TaskLogRepository {
    private final TaskLogJpaRepository taskLogJpaRepository;

    @Override
    public void save(TaskLogEntity taskLog) {
        taskLogJpaRepository.save(taskLog);
    }

    @Override
    public Optional<TaskLogEntity> findByTaskIdAndStaffId(final Long taskId, final Long staffId) {
        return taskLogJpaRepository.findByTaskIdAndStaffId(taskId, staffId);
    }

    @Override
    public TaskLogEntity getTaskLogByTaskIdAndStaffId(Long taskId, Long staffId) {
        return taskLogJpaRepository.findByTaskIdAndStaffId(taskId, staffId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.FORBIDDEN_TASK_LOG_DELETE));
    }

    @Override
    public void delete(TaskLogEntity taskLog) {
        taskLogJpaRepository.delete(taskLog);
    }

    @Override
    public List<TaskLogEntity> findByTaskIds(final List<Long> taskIds) {
        return taskLogJpaRepository.findByTaskIdIn(taskIds);
    }

    @Override
    public Optional<TaskLogEntity> findTaskLogByTaskId(Long taskId) {
        return taskLogJpaRepository.findByTaskId(taskId);
    }
}

