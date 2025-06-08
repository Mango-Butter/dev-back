package com.mangoboss.app.dto.attendance.base;

import jakarta.validation.constraints.NotBlank;

public interface QrRequest extends AttendanceBaseRequest {
    @NotBlank
    String qrCode();
}