package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.TaskRepository;
import com.mangoboss.storage.task.TaskEntity;
import com.mangoboss.storage.task.TaskJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Override
    public TaskEntity getTaskByIdAndStoreId(Long id, Long storeId) {
        return taskJpaRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.STORE_TASK_MISMATCH));
    }

    @Override
    public List<TaskEntity> findByStoreIdAndTaskDate(Long storeId, LocalDate taskDate) {
        return taskJpaRepository.findByStoreIdAndTaskDate(storeId, taskDate);
    }

}