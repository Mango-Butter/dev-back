package com.mangoboss.app.dto.store.request;

import com.mangoboss.storage.store.StoreType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record StoreUpdateRequest(
        @NotBlank
        String address,

        @NotNull
        StoreType storeType
) {}
