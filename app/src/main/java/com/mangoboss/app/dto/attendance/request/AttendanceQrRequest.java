package com.mangoboss.app.dto.attendance.request;

import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.app.dto.attendance.base.QrRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AttendanceQrRequest(
        @NotNull
        AttendanceMethod attendanceMethod,

        @NotNull
        Long scheduleId,

        @NotBlank
        String qrCode
) implements AttendanceBaseRequest, QrRequest {}
