package com.mangoboss.app.api.controller.faq;

import com.mangoboss.app.api.facade.faq.BossFaqFacade;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.faq.FaqResponse;
import com.mangoboss.storage.faq.FaqCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boss/faq")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossFaqController {

    private final BossFaqFacade bossFaqFacade;

    @GetMapping
    public ListWrapperResponse<FaqResponse> getFaqs(@RequestParam(value = "category", defaultValue = "ALL") FaqCategory category) {
        return ListWrapperResponse.of(bossFaqFacade.getFaqs(category));
    }
}