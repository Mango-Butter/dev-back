package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.AttendanceEditRepository;
import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.attendance.AttendanceEditJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceEditRepositoryImpl implements AttendanceEditRepository {
    private final AttendanceEditJpaRepository attendanceEditJpaRepository;

    @Override
    public void save(final AttendanceEditEntity attendanceEdit) {
        attendanceEditJpaRepository.save(attendanceEdit);
    }

    @Override
    public List<AttendanceEditEntity> findAllByStaffId(Long staffId) {
        return attendanceEditJpaRepository.findAllByStaffId(staffId);
    }
}
