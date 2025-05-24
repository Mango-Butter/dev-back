package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.TaskRoutineRepository;
import com.mangoboss.storage.task.TaskRoutineJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import com.mangoboss.storage.task.TaskRoutineEntity;

@RequiredArgsConstructor
@Repository
public class TaskRoutineRepositoryImpl implements TaskRoutineRepository {

    private final TaskRoutineJpaRepository taskRoutineJpaRepository;

    @Override
    public void save(TaskRoutineEntity entity) {
        taskRoutineJpaRepository.save(entity);
    }
}