package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.TaskRepository;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {
    private final TaskJpaRepository taskJpaRepository;

    @Override
    public void save(TaskEntity entity) {
        taskJpaRepository.save(entity);
    }

    @Override
    public void saveAll(List<TaskEntity> entities) {
        taskJpaRepository.saveAll(entities);
    }

}