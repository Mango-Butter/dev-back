package com.mangoboss.app.dto.workreport.request;

import com.mangoboss.storage.workreport.WorkReportTargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record WorkReportCreateRequest(
        @NotBlank String content,
        String reportImageUrl,
        @NotNull WorkReportTargetType targetType
) {}