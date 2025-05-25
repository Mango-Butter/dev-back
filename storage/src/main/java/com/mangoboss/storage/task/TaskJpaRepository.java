package com.mangoboss.storage.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findByIdAndStoreId(Long id, Long storeId);
}
