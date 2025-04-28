package com.mangoboss.app.dto.store.request;

import com.mangoboss.storage.store.AttendanceMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AttendanceSettingsRequest(
        @NotNull
        AttendanceMethod attendanceMethod
) {}