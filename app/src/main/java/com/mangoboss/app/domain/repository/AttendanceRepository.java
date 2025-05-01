package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.attendance.AttendanceEntity;

public interface AttendanceRepository {
    boolean existsByScheduleId(Long scheduleId);
    AttendanceEntity save(AttendanceEntity attendance);
    AttendanceEntity getById(Long id);
    AttendanceEntity getByScheduleId(Long scheduleId);
}
