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

    @Override
    public TaskEntity getTaskById(Long id) {
        return taskJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.TASK_NOT_FOUND));
    }

    @Override
    public TaskEntity getById(Long id) {
        return taskJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.TASK_NOT_FOUND));
    }

    @Override
    public void deleteAllByTaskRoutineId(final Long routineId) {
        taskJpaRepository.deleteAllByTaskRoutineId(routineId);
    }

    @Override
    public void deleteAllByTaskRoutineIdAndNotCompleted(final Long routineId) {
        taskJpaRepository.deleteAllByTaskRoutineIdAndNotCompleted(routineId);
    }

    @Override
    public void deleteRoutineReferenceForCompletedTasks(final Long routineId) {
        taskJpaRepository.clearRoutineReferenceForCompletedTasks(routineId);
    }

    @Override
    public void delete(TaskEntity task) {
        taskJpaRepository.delete(task);
    }
}