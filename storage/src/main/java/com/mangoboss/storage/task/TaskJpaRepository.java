package com.mangoboss.storage.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
    List<TaskEntity> findByStoreIdAndTaskDate(Long storeId, LocalDate taskDate);
    Optional<TaskEntity> findByIdAndStoreId(Long id, Long storeId);
}
