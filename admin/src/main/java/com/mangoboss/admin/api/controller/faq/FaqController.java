package com.mangoboss.admin.api.controller.faq;

import com.mangoboss.admin.api.facade.faq.FaqFacade;
import com.mangoboss.admin.dto.ListWrapperResponse;
import com.mangoboss.admin.dto.faq.request.FaqCreateRequest;
import com.mangoboss.admin.dto.faq.request.FaqUpdateRequest;
import com.mangoboss.admin.dto.faq.response.FaqResponse;
import com.mangoboss.storage.faq.FaqCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/faq")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class FaqController {

    private final FaqFacade faqFacade;

    @PostMapping
    public void createFaq(@RequestBody @Valid FaqCreateRequest request) {
        faqFacade.createFaq(request);
    }

    @PutMapping("/{faqId}")
    public void updateFaq(@PathVariable Long faqId,
                          @RequestBody @Valid FaqUpdateRequest request) {
        faqFacade.updateFaq(faqId, request);
    }

    @DeleteMapping("/{faqId}")
    public void deleteFaq(@PathVariable Long faqId) {
        faqFacade.deleteFaq(faqId);
    }

    @GetMapping
    public ListWrapperResponse<FaqResponse> getFaqs(@RequestParam(value = "category", defaultValue = "ALL") FaqCategory category) {
        return ListWrapperResponse.of(faqFacade.getFaqs(category));
    }
}
