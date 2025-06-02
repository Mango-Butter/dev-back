package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.attendance.AttendanceEditEntity;

import java.util.List;

public interface AttendanceEditRepository {
    void save(AttendanceEditEntity attendanceEdit);

    List<AttendanceEditEntity> findAllByStaffId(Long staffId);

    List<AttendanceEditEntity> findAllByStoreId(Long storeId);

    AttendanceEditEntity getById(Long attendanceEditId);
}
