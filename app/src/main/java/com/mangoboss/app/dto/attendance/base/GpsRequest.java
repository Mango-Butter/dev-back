package com.mangoboss.app.dto.attendance.base;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public interface GpsRequest {
    @NotNull
    Double latitude();

    @NotNull
    Double longitude();

    @NotNull
    LocalDateTime locationFetchedAt();
}