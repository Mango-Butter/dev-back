package com.mangoboss.app.dto.attendance.clock_in;

import com.mangoboss.app.dto.attendance.base.ClockInBaseRequest;
import com.mangoboss.app.dto.attendance.base.QrRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClockInQrRequest(
        @NotNull
        AttendanceMethod attendanceMethod,

        @NotNull
        Long scheduleId,

        @NotBlank
        String qrCode
) implements ClockInBaseRequest, QrRequest {}
