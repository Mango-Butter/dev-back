package com.mangoboss.app.dto.store.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AttendanceMethodUpdateRequest(
        @NotNull
        Boolean useQr,

        @NotNull
        Boolean useGps
) {
}