package com.mangoboss.app.dto.workreport.response;

import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.workreport.WorkReportEntity;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WorkReportResponse(
        Long workReportId,
        String content,
        String reportImageUrl,
        LocalDateTime createdAt,
        StaffSimpleResponse staff
) {
    public static WorkReportResponse fromEntity(final WorkReportEntity entity, final StaffEntity staffEntity) {
        return WorkReportResponse.builder()
                .workReportId(entity.getId())
                .content(entity.getContent())
                .reportImageUrl(entity.getReportImageUrl())
                .createdAt(entity.getCreatedAt())
                .staff(StaffSimpleResponse.fromEntity(staffEntity))
                .build();
    }
}