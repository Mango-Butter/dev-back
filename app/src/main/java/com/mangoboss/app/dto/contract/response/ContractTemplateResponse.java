package com.mangoboss.app.dto.contract.response;

import com.mangoboss.storage.contract.ContractTemplate;
import lombok.Builder;

@Builder
public record ContractTemplateResponse(
        Long templateId,

        String title

) {
    public static ContractTemplateResponse fromEntity(final ContractTemplate template) {
        return ContractTemplateResponse.builder()
                .templateId(template.getId())
                .title(template.getTitle())
                .build();
    }
}