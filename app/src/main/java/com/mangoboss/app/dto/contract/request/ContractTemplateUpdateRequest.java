package com.mangoboss.app.dto.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ContractTemplateUpdateRequest(
        @NotBlank
        String title,

        ContractTemplateData contractTemplateData
) {}