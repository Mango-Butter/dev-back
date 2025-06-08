package com.mangoboss.app.dto.contract.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ContractTemplateCreateRequest(
        @NotBlank
        String title,

        ContractTemplateData contractTemplateData
) {}