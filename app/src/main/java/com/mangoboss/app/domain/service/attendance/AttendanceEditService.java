package com.mangoboss.app.domain.service.attendance;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.AttendanceEditRepository;
import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.attendance.AttendanceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceEditService {
    private final AttendanceEditRepository attendanceEditRepository;

    @Transactional
    public void requestAttendanceEdit(final AttendanceEntity original, final AttendanceEditEntity attendanceEdit) {
        isUpdatable(original);
        attendanceEditRepository.save(attendanceEdit);
        original.requested();
    }

    private void isUpdatable(final AttendanceEntity attendance) {
        if (!attendance.isAlreadyClockedOut()) {
            throw new CustomException(CustomErrorInfo.INCOMPLETE_ATTENDANCE);
        }
        if(attendance.isRequested()){
            throw new CustomException(CustomErrorInfo.ATTENDANCE_ALREADY_REQUESTED);
        }
    }

    public List<AttendanceEditEntity> getAttendanceEditsByStaff(final Long staffId) {
        return attendanceEditRepository.findAllByStaffId(staffId);
    }
}
