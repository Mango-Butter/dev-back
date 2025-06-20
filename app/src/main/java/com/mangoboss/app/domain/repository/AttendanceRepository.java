package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.projection.WorkDotProjection;
import com.mangoboss.storage.attendance.projection.StaffAttendanceCountProjection;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {

    AttendanceEntity save(AttendanceEntity attendance);

    AttendanceEntity getById(Long id);

    List<WorkDotProjection> findWorkDotProjections(Long storeId, LocalDate start, LocalDate endDate);

    AttendanceEntity getByScheduleId(Long scheduleId);

    void delete(AttendanceEntity attendance);

    List<StaffAttendanceCountProjection> findAttendanceCountsByStoreId(Long storeId, LocalDate start, LocalDate end);

    List<AttendanceEntity> findByStaffIdAndWorkDateBetween(Long staffId, LocalDate start, LocalDate end);
}
