package com.mangoboss.batch.common.repository;

import com.mangoboss.storage.attendance.AttendanceEntity;

import java.util.List;

public interface AttendanceRepository {
    void saveAll(List<AttendanceEntity> attendances);
}
