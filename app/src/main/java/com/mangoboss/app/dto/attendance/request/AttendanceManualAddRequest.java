package com.mangoboss.app.dto.attendance.request;

import com.mangoboss.storage.schedule.ScheduleEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record AttendanceManualAddRequest(
        @NonNull
        Long staffId,

        @NonNull
        LocalDate workDate,

        LocalTime clockInTime,

        LocalTime clockOutTime
) {
    public ScheduleEntity toSchedule(final StaffEntity staff) {
        return ScheduleEntity.create(workDate, clockInTime, clockOutTime,
                staff, null, staff.getStore().getId());
    }
}
