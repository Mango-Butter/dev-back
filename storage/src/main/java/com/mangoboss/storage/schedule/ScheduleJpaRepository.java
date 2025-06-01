package com.mangoboss.storage.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {

    @Query("SELECT s FROM ScheduleEntity s " +
            "JOIN StaffEntity f ON s.staff.id = f.id " +
            "WHERE f.store.id = :storeId " +
            "AND s.workDate = :date " +
            "ORDER BY s.startTime ASC ")
    List<ScheduleEntity> findAllByStoreIdAndWorkDate(Long storeId, LocalDate date);

    void deleteAllByRegularGroupIdAndWorkDateAfter(Long regularGroupId, LocalDate date);

    Optional<ScheduleEntity> findByIdAndStaffId(Long id, Long staffId);

    Optional<ScheduleEntity> findByIdAndAttendanceIsNotNull(Long id);

    List<ScheduleEntity> findAllByStaffIdAndWorkDate(Long staffId, LocalDate date);

    @Query("SELECT s FROM ScheduleEntity s " +
            "LEFT JOIN s.attendance a " +
            "WHERE s.endTime <= :oneHourAgo " +
            "AND (a.clockOutStatus is NULL OR a is NULL) ")
    List<ScheduleEntity> findAllSchedulesWithoutClockOut(LocalDateTime oneHourAgo);

    Boolean existsByRegularGroupId(Long regularGroupId);

    @Query("""
            SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
            FROM ScheduleEntity s
            WHERE s.staff.id = :staffId
            AND s.workDate = :workDate
            AND s.startTime <= :endTime
            AND s.endTime >= :startTime
            """)
    Boolean existsOverlappingSchedule(Long staffId, LocalDate workDate, LocalDateTime startTime, LocalDateTime endTime);
}
