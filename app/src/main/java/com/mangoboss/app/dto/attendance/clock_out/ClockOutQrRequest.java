package com.mangoboss.app.dto.attendance.clock_out;

import com.mangoboss.app.dto.attendance.base.ClockOutBaseRequest;
import com.mangoboss.app.dto.attendance.base.QrRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClockOutQrRequest(
        @NotNull
        AttendanceMethod attendanceMethod,

        @NotNull
        Long attendanceId,

        @NotBlank
        String qrCode
) implements ClockOutBaseRequest, QrRequest {}
