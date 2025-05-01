package com.mangoboss.storage.attendance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, Long> {
	boolean existsByScheduleId(Long scheduleId);
}
