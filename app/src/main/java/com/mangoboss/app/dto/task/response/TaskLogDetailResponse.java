package com.mangoboss.app.dto.task.response;

import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TaskLogDetailResponse(
        String taskLogImageUrl,
        LocalDateTime createdAt,
        StaffSimpleResponse checkedStaff
) {
    public static TaskLogDetailResponse of(
            final String taskLogImageUrl,
            final LocalDateTime createdAt,
            final StaffSimpleResponse staff
    ) {
        return TaskLogDetailResponse.builder()
                .taskLogImageUrl(taskLogImageUrl)
                .createdAt(createdAt)
                .checkedStaff(staff)
                .build();
    }
}
