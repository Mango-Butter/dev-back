package com.mangoboss.app.dto.attendance.request;

import com.mangoboss.storage.attendance.ClockInStatus;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record AttendanceUpdateRequest(
        @NonNull
        LocalTime clockInTime,

        @NonNull
        LocalTime clockOutTime,

        @NonNull
        ClockInStatus clockInStatus
) {
    public LocalDateTime toClockInDateTime(final LocalDate workDate) {
        return LocalDateTime.of(workDate, clockInTime);
    }

    public LocalDateTime toClockOutDateTime(final LocalDate workDate) {
        if (clockOutTime.isAfter(clockInTime)) {
            return LocalDateTime.of(workDate, clockOutTime);
        } else {
            return LocalDateTime.of(workDate.plusDays(1), clockOutTime);
        }
    }
}
