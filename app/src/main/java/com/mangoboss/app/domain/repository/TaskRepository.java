package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.task.TaskEntity;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository {
    void save(TaskEntity entity);
    void saveAll(List<TaskEntity> entities);
    TaskEntity getTaskByIdAndStoreId(Long id, Long storeId);
}
