package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.task.TaskRoutineEntity;

public interface TaskRoutineRepository {
    void save(TaskRoutineEntity taskRoutineEntity);
}