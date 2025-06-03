package com.mangoboss.app.dto.store.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record GpsRequest(
        @NotNull
        Double latitude,

        @NotNull
        Double longitude
) {}
