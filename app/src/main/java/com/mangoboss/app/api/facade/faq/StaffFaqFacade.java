package com.mangoboss.app.api.facade.faq;

import com.mangoboss.app.domain.service.faq.FaqService;
import com.mangoboss.app.dto.faq.FaqResponse;
import com.mangoboss.storage.faq.FaqCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StaffFaqFacade {

    private final FaqService faqService;

    public List<FaqResponse> getFaqs(final FaqCategory category) {
        return faqService.getFaqs(category).stream()
                .map(FaqResponse::fromEntity)
                .collect(Collectors.toList());
    }
}