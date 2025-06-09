package com.mangoboss.storage.schedule;

import com.mangoboss.storage.schedule.projection.ScheduleForLateClockInProjection;
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
            "WHERE s.endTime <= :standardTime " +
            "AND (a.clockOutStatus is NULL OR a is NULL) ")
    List<ScheduleEntity> findAllSchedulesWithoutClockOut(LocalDateTime standardTime);

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

    @Query("""
                SELECT s AS schedule, f.name AS staffName, st.boss.id AS bossId, st.id AS storeId
                FROM ScheduleEntity s
                JOIN StaffEntity f ON s.staff.id = f.id
                JOIN StoreEntity st ON f.store.id = st.id
                LEFT JOIN s.attendance a
                WHERE s.startTime < :standardTime
                  AND a IS NULL
                  AND NOT EXISTS (
                      SELECT 1 FROM NotificationEntity n
                      WHERE n.type = 'SCHEDULE'
                        AND n.metaId = s.id
                  )
            """)
    List<ScheduleForLateClockInProjection> findLateSchedulesWithoutAlarm(LocalDateTime standardTime);
}
