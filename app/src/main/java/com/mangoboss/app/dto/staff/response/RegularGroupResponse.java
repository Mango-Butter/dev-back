package com.mangoboss.app.dto.staff.response;

import com.mangoboss.storage.schedule.RegularGroupEntity;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record RegularGroupResponse(
        Long regularGroupId,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        LocalDate repeatStartDate,
        LocalDate repeatEndDate
) {
    public static RegularGroupResponse fromEntity(final RegularGroupEntity regularGroup) {
        return RegularGroupResponse.builder()
                .regularGroupId(regularGroup.getId())
                .dayOfWeek(regularGroup.getDayOfWeek())
                .startTime(regularGroup.getStartTime())
                .endTime(regularGroup.getEndTime())
                .repeatStartDate(regularGroup.getRepeatStartDate())
                .repeatEndDate(regularGroup.getRepeatEndDate())
                .build();
    }
}
