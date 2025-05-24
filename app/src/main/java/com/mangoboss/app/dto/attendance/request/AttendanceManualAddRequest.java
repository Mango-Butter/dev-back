package com.mangoboss.app.dto.attendance.request;

import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AttendanceManualAddRequest(
        @NotNull
        Long staffId,

        @NotNull
        LocalDate workDate,

        LocalTime clockInTime,

        LocalTime clockOutTime
) {
    public ScheduleEntity toSchedule(final StaffEntity staff) {
        return ScheduleEntity.create(workDate, clockInTime, clockOutTime,
                staff, null, staff.getStore().getId());
    }
}
