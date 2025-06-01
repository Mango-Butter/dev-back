package com.mangoboss.app.dto.schedule.response;

import com.mangoboss.storage.schedule.SubstituteRequestEntity;
import com.mangoboss.storage.schedule.SubstituteState;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record SubstituteRequestResponse(
        String requesterName,
        String targetName,
        String reason,
        SubstituteState state,
        LocalDate workDate,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static SubstituteRequestResponse fromEntity(
            SubstituteRequestEntity substituteRequest
    ) {
        return SubstituteRequestResponse.builder()
                .requesterName(substituteRequest.getRequesterStaffName())
                .targetName(substituteRequest.getTargetStaffName())
                .reason(substituteRequest.getReason())
                .state(substituteRequest.getState())
                .workDate(substituteRequest.getWorkDate())
                .startTime(substituteRequest.getStartTime())
                .endTime(substituteRequest.getEndTime())
                .build();
    }
}
