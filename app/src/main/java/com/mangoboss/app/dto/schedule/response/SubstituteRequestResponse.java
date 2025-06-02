package com.mangoboss.app.dto.schedule.response;

import com.mangoboss.storage.schedule.SubstituteRequestEntity;
import com.mangoboss.storage.schedule.SubstituteRequestState;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record SubstituteRequestResponse(
        Long substituteRequestId,
        String requesterName,
        String targetName,
        String reason,
        SubstituteRequestState substituteRequestState,
        LocalDate workDate,
        LocalDateTime startTime,
        LocalDateTime endTime,
        LocalDateTime createdAt
) {
    public static SubstituteRequestResponse fromEntity(
            SubstituteRequestEntity substituteRequest
    ) {
        return SubstituteRequestResponse.builder()
                .substituteRequestId(substituteRequest.getId())
                .requesterName(substituteRequest.getRequesterStaffName())
                .targetName(substituteRequest.getTargetStaffName())
                .reason(substituteRequest.getReason())
                .substituteRequestState(substituteRequest.getState())
                .workDate(substituteRequest.getWorkDate())
                .startTime(substituteRequest.getStartTime())
                .endTime(substituteRequest.getEndTime())
                .createdAt(substituteRequest.getCreatedAt())
                .build();
    }
}
