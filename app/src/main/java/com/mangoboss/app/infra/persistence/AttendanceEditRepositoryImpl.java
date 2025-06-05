package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.AttendanceEditRepository;
import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.attendance.AttendanceEditJpaRepository;
import com.mangoboss.storage.attendance.AttendanceEditState;
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
    public List<AttendanceEditEntity> findAllByStaffId(final Long staffId) {
        return attendanceEditJpaRepository.findAllByStaffId(staffId);
    }

    @Override
    public List<AttendanceEditEntity> findAllByStoreId(final Long storeId) {
        return attendanceEditJpaRepository.findAllByStoreId(storeId);
    }

    @Override
    public AttendanceEditEntity getById(final Long attendanceEditId) {
        return attendanceEditJpaRepository.findById(attendanceEditId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.ATTENDANCE_EDIT_NOT_FOUND));
    }

    @Override
    public List<AttendanceEditEntity> findRecentIncompleteEditsByStoreId(final Long storeId) {
        return attendanceEditJpaRepository.findAllByStoreIdAndAttendanceEditStateOrderByCreatedAtDesc(storeId, AttendanceEditState.PENDING);
    }
}
