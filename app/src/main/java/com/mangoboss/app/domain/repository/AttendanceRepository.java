package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.projection.WorkDotProjection;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {
    boolean existsByScheduleId(Long scheduleId);

    AttendanceEntity save(AttendanceEntity attendance);

    AttendanceEntity getById(Long id);

    AttendanceEntity getByScheduleId(Long scheduleId);

    List<WorkDotProjection> findWorkDotProjections(Long storeId, LocalDate start, LocalDate endDate);
}
