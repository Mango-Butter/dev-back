package com.mangoboss.storage.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {

    @Query("SELECT s FROM ScheduleEntity s " +
            "JOIN StaffEntity f ON s.staff.id = f.id " +
            "WHERE f.store.id = :storeId " +
            "AND s.workDate = :date " +
            "ORDER BY s.startTime ASC ")
    List<ScheduleEntity> findAllByStoreIdAndWorkDate(Long storeId, LocalDate date);
}
