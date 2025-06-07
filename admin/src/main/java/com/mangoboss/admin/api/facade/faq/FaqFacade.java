package com.mangoboss.admin.api.facade.faq;

import com.mangoboss.admin.domain.service.FaqService;
import com.mangoboss.admin.dto.faq.request.FaqCreateRequest;
import com.mangoboss.admin.dto.faq.request.FaqUpdateRequest;
import com.mangoboss.admin.dto.faq.response.FaqResponse;
import com.mangoboss.storage.faq.FaqCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FaqFacade {

    private final FaqService faqService;

    public void createFaq(final FaqCreateRequest request) {
        faqService.createFaq(request.toEntity());
    }

    public void updateFaq(final Long faqId, final FaqUpdateRequest request) {
        faqService.updateFaq(faqId, request.category(), request.question(), request.answer());
    }

    public void deleteFaq(final Long faqId) {
        faqService.deleteFaq(faqId);
    }

    public List<FaqResponse> getFaqs(final FaqCategory category) {
        return faqService.getFaqs(category).stream()
                .map(FaqResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
