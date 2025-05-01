package com.mangoboss.app.dto.attendance.request;

import com.mangoboss.app.dto.attendance.base.*;
import com.mangoboss.storage.store.AttendanceMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AttendanceBothRequest(
        @NotNull
        AttendanceMethod attendanceMethod,

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
) implements AttendanceBaseRequest, BothRequest {}
