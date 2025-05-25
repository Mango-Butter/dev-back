package com.mangoboss.batch.clock_out;

import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.AttendanceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {
    private final AttendanceJpaRepository attendanceJpaRepository;

    @Override
    public void saveAll(final List<AttendanceEntity> attendances) {
        attendanceJpaRepository.saveAll(attendances);
    }
}
