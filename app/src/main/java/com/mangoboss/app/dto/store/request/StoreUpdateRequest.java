package com.mangoboss.app.dto.store.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record StoreUpdateRequest(
        @NotBlank
        String address,
        String chatLink
) {}
