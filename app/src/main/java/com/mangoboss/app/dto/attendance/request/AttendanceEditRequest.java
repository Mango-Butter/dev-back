package com.mangoboss.app.dto.attendance.request;

import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.attendance.ClockInStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AttendanceEditRequest(
        @NotBlank
        String reason,

        @NonNull
        LocalTime requestedClockInTime,

        @NonNull
        LocalTime requestedClockOutTime,

        @NonNull
        ClockInStatus requestedClockInStatus
) {
    public AttendanceEditEntity toEntity(final AttendanceEntity attendance) {
        LocalDate workDate = attendance.getSchedule().getWorkDate();
        return AttendanceEditEntity.create(
                attendance,
                getLocalDateTimeForClockInTime(workDate),
                getLocalDateTimeForClockOutTime(workDate),
                requestedClockInStatus,
                reason
        );
    }

    public LocalDateTime getLocalDateTimeForClockInTime(final LocalDate workDate) {
        return LocalDateTime.of(
                workDate,
                requestedClockInTime
        );
    }

    public LocalDateTime getLocalDateTimeForClockOutTime(final LocalDate workDate) {
        return LocalDateTime.of(
                workDate,
                requestedClockOutTime
        );
    }
}
