package com.mangoboss.storage.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskLogJpaRepository extends JpaRepository<TaskLogEntity, Long> {

    Optional<TaskLogEntity> findByTaskIdAndStaffId(Long taskId, Long staffId);
}

