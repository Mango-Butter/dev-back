package com.mangoboss.app.dto.attendance.request;

import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Builder
public record AttendanceManualAddRequest(
        @NonNull
        Long staffId,

        @NonNull
        LocalDate workDate,

        @NonNull
        LocalTime startTime,

        @NonNull
        LocalTime endTime
) {
    public ScheduleEntity toSchedule(final StaffEntity staff) {
        return ScheduleEntity.create(workDate, LocalDateTime.of(workDate, startTime), LocalDateTime.of(workDate, endTime),
                staff, null, staff.getStore().getId());
    }

    public LocalDateTime toStartDateTime() {
        return LocalDateTime.of(workDate, startTime);
    }

    public LocalDateTime toEndDateTime() {
        return LocalDateTime.of(workDate, endTime);
    }
}
