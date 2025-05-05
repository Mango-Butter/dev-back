package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.AttendanceRepository;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.AttendanceJpaRepository;
import com.mangoboss.storage.attendance.projection.WorkDotProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {
    private final AttendanceJpaRepository attendanceJpaRepository;

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
    public List<WorkDotProjection> findWorkDotProjections(Long storeId, LocalDate start, LocalDate end) {
        return attendanceJpaRepository.findWorkDotProjections(storeId, start, end);
    }
}
