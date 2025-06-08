package com.mangoboss.storage.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RegularGroupJpaRepository extends JpaRepository<RegularGroupEntity, Long> {
    @Query("""
            SELECT r
            FROM RegularGroupEntity  r JOIN ScheduleEntity  s ON r.id = s.regularGroup.id
            WHERE r.staff.id = :staffId AND r.endDate >= :date AND s.workDate >= :date
            """)
    List<RegularGroupEntity> findActiveOrUpcomingByStaffId(Long staffId, LocalDate date);
}
