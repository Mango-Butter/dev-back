package com.mangoboss.app.dto.attendance.clock_in;

import com.mangoboss.app.dto.attendance.base.ClockInBaseRequest;
import com.mangoboss.app.dto.attendance.base.GpsRequest;
import com.mangoboss.app.dto.attendance.base.QrRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ClockInBothRequest(
        @NotBlank
        String attendanceMethod,

        @NotNull
        Long scheduleId,

        @NotBlank
        String qrCode,

        @NotNull
        Double latitude,

        @NotNull
        Double longitude,

        @NotNull
        LocalDateTime locationFetchedAt
) implements ClockInBaseRequest, QrRequest, GpsRequest {}
