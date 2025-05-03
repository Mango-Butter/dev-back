package com.mangoboss.app.dto.schedule.request;

import com.mangoboss.storage.schedule.ScheduleEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record ScheduleCreateRequest(
        @NonNull
        Long staffId,

        @NonNull
        LocalDate workDate,

        @NonNull
        LocalTime startTime,

        @NonNull
        LocalTime endTime
) {
    public ScheduleEntity toEntity(final StaffEntity staff, final Long storeId) {
        return ScheduleEntity.create(workDate, LocalDateTime.of(workDate, startTime), LocalDateTime.of(workDate, endTime), staff, null, storeId);
    }
}
