package com.mangoboss.app.dto.staff.request;

import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;
import lombok.NonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record RegularGroupCreateRequest(
        @NonNull
        DayOfWeek dayOfWeek,

        @NonNull
        LocalDate startDate,

        @NonNull
        LocalDate endDate,

        @NonNull
        LocalTime startTime,

        @NonNull
        LocalTime endTime
){
    public RegularGroupEntity toEntity(final StaffEntity staff){
        return RegularGroupEntity.create(
                dayOfWeek, startTime, endTime, startDate, endDate, staff);
    }
}
