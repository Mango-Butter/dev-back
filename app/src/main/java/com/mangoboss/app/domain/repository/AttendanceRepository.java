package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.projection.WorkDotProjection;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {

    AttendanceEntity save(AttendanceEntity attendance);

    AttendanceEntity getById(Long id);

    List<WorkDotProjection> findWorkDotProjections(Long storeId, LocalDate start, LocalDate endDate);

    AttendanceEntity getByScheduleId(Long scheduleId);
}
