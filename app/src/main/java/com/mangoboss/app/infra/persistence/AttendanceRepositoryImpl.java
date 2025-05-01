package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.AttendanceRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.AttendanceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {
    private final AttendanceJpaRepository attendanceJpaRepository;

    @Override
    public boolean existsByScheduleId(final Long scheduleId) {
        return attendanceJpaRepository.existsByScheduleId(scheduleId);
    }

    @Override
    public AttendanceEntity save(final AttendanceEntity attendance) {
        return attendanceJpaRepository.save(attendance);
    }

    @Override
    public AttendanceEntity getById(final Long attendanceId) {
        return attendanceJpaRepository.findById(attendanceId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.ATTENDANCE_NOT_FOUND));
    }

    @Override
    public AttendanceEntity getByScheduleId(Long scheduleId) {
        return attendanceJpaRepository.findByScheduleId(scheduleId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.NOT_CLOCKED_IN_YET));
    }
}
