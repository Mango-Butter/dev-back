package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.task.TaskLogEntity;

import java.util.List;
import java.util.Optional;

public interface TaskLogRepository {
    void save(TaskLogEntity taskLog);
    Optional<TaskLogEntity> findByTaskIdAndStaffId(Long taskId, Long staffId);
    TaskLogEntity getTaskLogByTaskIdAndStaffId(Long taskId, Long staffId);
    void delete(TaskLogEntity taskLog);
    List<TaskLogEntity> findByTaskIds(List<Long> taskIds);
}