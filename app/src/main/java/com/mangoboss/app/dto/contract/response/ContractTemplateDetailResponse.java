package com.mangoboss.app.dto.contract.response;

import com.mangoboss.app.dto.contract.request.ContractTemplateData;
import lombok.Builder;

@Builder
public record ContractTemplateDetailResponse(
        String title,

        ContractTemplateData contractTemplateData
) {
    public static ContractTemplateDetailResponse of(final String title, final ContractTemplateData data) {
        return ContractTemplateDetailResponse.builder()
                .title(title)
                .contractTemplateData(data)
                .build();
    }
}