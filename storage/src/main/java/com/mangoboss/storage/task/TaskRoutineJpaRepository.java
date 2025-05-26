package com.mangoboss.storage.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRoutineJpaRepository extends JpaRepository<TaskRoutineEntity, Long> {
    List<TaskRoutineEntity> findAllByStoreId(Long storeId);
}