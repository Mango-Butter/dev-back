package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.task.TaskEntity;

import java.util.List;

public interface TaskRepository {
    void save(TaskEntity entity);
    void saveAll(List<TaskEntity> entities);
}
