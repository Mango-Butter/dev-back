package com.mangoboss.app.dto.faq;

import com.mangoboss.storage.faq.FaqCategory;
import com.mangoboss.storage.faq.FaqEntity;
import lombok.Builder;

@Builder
public record FaqResponse(
        Long id,
        FaqCategory category,
        String question,
        String answer
) {
    public static FaqResponse fromEntity(final FaqEntity entity) {
        return FaqResponse.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .question(entity.getQuestion())
                .answer(entity.getAnswer())
                .build();
    }
}