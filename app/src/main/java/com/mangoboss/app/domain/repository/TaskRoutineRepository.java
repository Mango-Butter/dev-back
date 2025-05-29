package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.task.TaskRoutineEntity;

import java.util.List;

public interface TaskRoutineRepository {
    void save(TaskRoutineEntity taskRoutineEntity);
    List<TaskRoutineEntity> findAllByStoreId(Long storeId);
    void delete(TaskRoutineEntity routine);
}