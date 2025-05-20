package com.mangoboss.app.dto.attendance.request;

import com.mangoboss.storage.attendance.ClockInStatus;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record AttendanceUpdateRequest(

        LocalTime clockInTime,

        LocalTime clockOutTime,

        @NonNull
        ClockInStatus clockInStatus
) {
    public LocalDateTime toClockInDateTime(final LocalDate workDate) {
        return clockInTime == null ? null : LocalDateTime.of(workDate, clockInTime);
    }

    public LocalDateTime toClockOutDateTime(final LocalDate workDate) {
        if (clockOutTime == null) {
            return null;
        }
        if (clockOutTime.isAfter(clockInTime)) {
            return LocalDateTime.of(workDate, clockOutTime);
        }
        return LocalDateTime.of(workDate.plusDays(1), clockOutTime);
    }
}
