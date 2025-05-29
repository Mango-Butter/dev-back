package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.TaskRoutineRepository;
import com.mangoboss.storage.task.TaskRoutineJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import com.mangoboss.storage.task.TaskRoutineEntity;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TaskRoutineRepositoryImpl implements TaskRoutineRepository {

    private final TaskRoutineJpaRepository taskRoutineJpaRepository;

    @Override
    public void save(TaskRoutineEntity entity) {
        taskRoutineJpaRepository.save(entity);
    }

    @Override
    public TaskRoutineEntity getByIdAndStoreId(final Long routineId, final Long storeId) {
        return taskRoutineJpaRepository.findByIdAndStoreId(routineId, storeId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.TASK_ROUTINE_NOT_FOUND));
    }

    @Override
    public List<TaskRoutineEntity> findAllByStoreId(final Long storeId) {
        return taskRoutineJpaRepository.findAllByStoreId(storeId);
    }

    @Override
    public void delete(final TaskRoutineEntity routine) {
        taskRoutineJpaRepository.delete(routine);
    }
}