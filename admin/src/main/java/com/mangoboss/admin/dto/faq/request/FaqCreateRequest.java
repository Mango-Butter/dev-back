package com.mangoboss.admin.dto.faq.request;

import com.mangoboss.storage.faq.FaqCategory;
import com.mangoboss.storage.faq.FaqEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FaqCreateRequest(
        @NotNull
        FaqCategory category,

        @NotBlank
        String question,

        @NotBlank
        String answer
) {
    public FaqEntity toEntity() {
        return FaqEntity.create(category, question, answer);
    }
}