package com.mangoboss.storage.attendance;

import com.mangoboss.storage.attendance.projection.WorkDotProjection;
import com.mangoboss.storage.attendance.projection.StaffAttendanceCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, Long> {
    boolean existsByScheduleId(Long scheduleId);

    Optional<AttendanceEntity> findByScheduleId(Long scheduleId);

    @Query("""
               SELECT DATE(s.workDate) AS date,
                   SUM(CASE WHEN a.clockInStatus = 'NORMAL' THEN 1 ELSE 0 END) AS normalCount,
                   SUM(CASE WHEN a.clockInStatus = 'LATE'
                    THEN 1 ELSE 0 END) AS lateCount,
                   SUM(CASE WHEN a.clockInStatus = 'ABSENT' THEN 1 ELSE 0 END) AS absentCount,
                   COUNT(s.id)-COUNT(a.id) AS preScheduleCount
               FROM ScheduleEntity s
               LEFT JOIN AttendanceEntity a ON a.schedule.id = s.id
               WHERE s.storeId = :storeId AND s.workDate BETWEEN :start AND :end
               GROUP BY DATE(s.workDate)
               ORDER BY DATE(s.workDate)
            """)
    List<WorkDotProjection> findWorkDotProjections(Long storeId, LocalDate start, LocalDate end);

    @Query("""
                SELECT s AS staff,
                    SUM(CASE WHEN a.clockInStatus = 'NORMAL' THEN 1 ELSE 0 END) AS normalCount,
                    SUM(CASE WHEN a.clockInStatus = 'LATE' THEN 1 ELSE 0 END) AS lateCount,
                    SUM(CASE WHEN a.clockInStatus = 'ABSENT' THEN 1 ELSE 0 END) AS absentCount
                FROM StaffEntity s
                LEFT JOIN AttendanceEntity a
                    ON a.schedule.staff.id = s.id
                    AND a.schedule.workDate BETWEEN :start AND :end
                WHERE s.store.id =:storeId
                GROUP BY s.id
            """)
    List<StaffAttendanceCountProjection> findAttendanceCountsByStoreId(Long storeId, LocalDate start, LocalDate end);

    @Query("""
                SELECT a FROM AttendanceEntity a
                WHERE a.schedule.staff.id = :staffId
                AND a.schedule.workDate BETWEEN :start AND :end
            """)
    List<AttendanceEntity> findByStaffIdAndWorkDateBetween(@Param("staffId") Long staffId, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
