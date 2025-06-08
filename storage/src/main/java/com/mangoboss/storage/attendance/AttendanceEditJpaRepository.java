package com.mangoboss.storage.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceEditJpaRepository extends JpaRepository<AttendanceEditEntity, Long> {
    List<AttendanceEditEntity> findAllByStaffId(Long staffId);

    List<AttendanceEditEntity> findAllByStoreId(Long storeId);

    List<AttendanceEditEntity> findAllByStoreIdAndAttendanceEditStateOrderByCreatedAtDesc(Long storeId, AttendanceEditState requestState);
}
