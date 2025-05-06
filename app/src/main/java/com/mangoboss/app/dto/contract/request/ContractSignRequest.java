package com.mangoboss.app.dto.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ContractSignRequest(
        @NotBlank
        String staffSignatureKey
) {}
