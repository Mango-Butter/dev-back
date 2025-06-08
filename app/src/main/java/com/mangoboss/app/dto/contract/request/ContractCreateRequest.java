package com.mangoboss.app.dto.contract.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ContractCreateRequest(
        @NotNull
        Long staffId,

        @NotBlank
        String bossSignatureKey,

        @NotNull
        ContractDataInput contractDataInput
) {}