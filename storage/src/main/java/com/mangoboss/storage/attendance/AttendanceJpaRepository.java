package com.mangoboss.storage.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, Long> {
	boolean existsByScheduleId(Long scheduleId);
	Optional<AttendanceEntity> findByScheduleId(Long scheduleId);
}
