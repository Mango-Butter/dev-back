package com.mangoboss.app.dto.attendance.clock_out;

import com.mangoboss.app.dto.attendance.base.ClockOutBaseRequest;
import com.mangoboss.app.dto.attendance.base.QrRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClockOutQrRequest(
        @NotBlank String attendanceMethod,
        @NotNull Long attendanceId,
        @NotBlank String qrCode
) implements ClockOutBaseRequest, QrRequest {}
