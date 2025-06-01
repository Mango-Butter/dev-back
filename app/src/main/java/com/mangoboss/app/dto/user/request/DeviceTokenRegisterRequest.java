package com.mangoboss.app.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DeviceTokenRegisterRequest(
        @NotBlank
        String fcmToken
) {}
