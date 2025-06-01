package com.mangoboss.app.dto.attendance.response;

import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.attendance.AttendanceEditState;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record AttendanceEditResponse(
        Long attendanceEditId,
        String staffName,
        AttendanceEditState attendanceEditState,
        LocalDate workDate,
        AttendanceSimpleForEditResponse originalAttendance,
        AttendanceSimpleForEditResponse requestedAttendance,
        LocalDateTime createdAt
) {
    public static AttendanceEditResponse fromEntity(final AttendanceEditEntity attendanceEdit) {
        return AttendanceEditResponse.builder()
                .attendanceEditId(attendanceEdit.getId())
                .attendanceEditState(attendanceEdit.getAttendanceEditState())
                .staffName(attendanceEdit.getStaffName())
                .workDate(attendanceEdit.getOriginalWorkDate())
                .originalAttendance(AttendanceSimpleForEditResponse.ofOriginal(attendanceEdit))
                .requestedAttendance(AttendanceSimpleForEditResponse.ofRequested(attendanceEdit))
                .createdAt(attendanceEdit.getCreatedAt())
                .build();
    }
}
