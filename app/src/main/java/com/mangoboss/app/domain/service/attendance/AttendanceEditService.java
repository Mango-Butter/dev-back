package com.mangoboss.app.domain.service.attendance;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.AttendanceEditRepository;
import com.mangoboss.app.domain.repository.AttendanceRepository;
import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import com.mangoboss.storage.attendance.ClockOutStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceEditService {
    private static final long CLOCK_ALLOWED_MINUTES = 10;

    private final AttendanceEditRepository attendanceEditRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public void requestAttendanceEdit(final AttendanceEntity original, final AttendanceEditEntity attendanceEdit) {
        validateUpdatable(original);
        attendanceEditRepository.save(attendanceEdit);
        original.requested();
    }

    private void validateUpdatable(final AttendanceEntity attendance) {
        if (!attendance.isAlreadyClockedOut()) {
            throw new CustomException(CustomErrorInfo.INCOMPLETE_ATTENDANCE);
        }
        if(attendance.isRequested()){
            throw new CustomException(CustomErrorInfo.ATTENDANCE_ALREADY_REQUESTED);
        }
    }

    private void validateRequested(final AttendanceEntity attendance) {
        if (!attendance.isRequested()) {
            throw new CustomException(CustomErrorInfo.ATTENDANCE_EDIT_NOT_REQUESTED);
        }
    }

    public List<AttendanceEditEntity> getAttendanceEditsByStaff(final Long staffId) {
        return attendanceEditRepository.findAllByStaffId(staffId);
    }

    public List<AttendanceEditEntity> getAttendanceByStoreId(final Long storeId) {
        return attendanceEditRepository.findAllByStoreId(storeId);
    }

    @Transactional
    public AttendanceEditEntity approveAttendanceEdit(final Long attendanceEditId) {
        AttendanceEditEntity attendanceEdit = attendanceEditRepository.getById(attendanceEditId);
        AttendanceEntity attendance = attendanceRepository.getById(attendanceEdit.getAttendanceId());
        validateRequested(attendance);
        LocalDateTime clockInTime = attendanceEdit.getRequestedClockInTime();
        LocalDateTime clockOutTime = attendanceEdit.getRequestedClockOutTime();
        ClockInStatus clockInStatus = attendanceEdit.getRequestedClockInStatus();
        ClockOutStatus clockOutStatus = determineClockOutStatus(
                attendance.getSchedule().getEndTime(),
                clockOutTime
        );
        attendance.update(clockInTime, clockOutTime, clockInStatus, clockOutStatus).edited();
        return attendanceEdit.approved();
    }

    private ClockOutStatus determineClockOutStatus(final LocalDateTime scheduledEndTime, final LocalDateTime clockOutTime) {
        final LocalDateTime limitTime = scheduledEndTime.plusMinutes(CLOCK_ALLOWED_MINUTES);
        if (clockOutTime.isBefore(scheduledEndTime)) {
            return ClockOutStatus.EARLY_LEAVE;
        }
        if (clockOutTime.isAfter(limitTime)) {
            return ClockOutStatus.OVERTIME;
        }

        return ClockOutStatus.NORMAL;
    }

    @Transactional
    public AttendanceEditEntity rejectAttendanceEdit(final Long attendanceEditId) {
        AttendanceEditEntity attendanceEdit = attendanceEditRepository.getById(attendanceEditId);
        AttendanceEntity attendance = attendanceRepository.getById(attendanceEdit.getAttendanceId());
        validateRequested(attendance);
        attendance.none();
        return attendanceEdit.rejected();
    }
}
