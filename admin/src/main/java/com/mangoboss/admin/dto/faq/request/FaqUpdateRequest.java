package com.mangoboss.admin.dto.faq.request;

import com.mangoboss.storage.faq.FaqCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FaqUpdateRequest(
        @NotNull
        FaqCategory category,

        @NotBlank
        String question,

        @NotBlank
        String answer
) {
}