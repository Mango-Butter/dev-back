package com.mangoboss.app.dto.attendance.base;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public interface GpsRequest extends AttendanceBaseRequest {
    @NotNull
    Double latitude();

    @NotNull
    Double longitude();

    @NotNull
    LocalDateTime locationFetchedAt();
}