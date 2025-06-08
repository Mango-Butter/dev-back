package com.mangoboss.app.dto.billing;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record BillingRegisterRequest(
        @NotBlank String customerKey,
        @NotBlank String authKey
) { }