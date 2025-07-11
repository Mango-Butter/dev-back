package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.schedule.ScheduleEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository {
    ScheduleEntity save(ScheduleEntity schedule);

    List<ScheduleEntity> findAllByStoreIdAndWorkDate(Long storeId, LocalDate date);

    void delete(ScheduleEntity schedule);

    ScheduleEntity getById(Long id);

    void deleteAllByRegularGroupIdAndWorkDateAfter(Long regularGroupId, LocalDate date);

    ScheduleEntity getByIdAndStaffId(Long scheduleId, Long staffId);

    ScheduleEntity getByIdAndAttendanceIsNotNull(Long scheduleId);

    List<ScheduleEntity> findAllByStaffIdAndWorkDate(Long staffId, LocalDate date);

    Boolean existsByRegularGroupId(Long regularGroupId);

    Boolean existsOverlappingSchedule(Long staffId, LocalDate workDate,
                                      LocalDateTime startTime, LocalDateTime endTime);
}
