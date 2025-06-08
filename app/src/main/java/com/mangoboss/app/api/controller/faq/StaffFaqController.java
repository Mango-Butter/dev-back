package com.mangoboss.app.api.controller.faq;

import com.mangoboss.app.api.facade.faq.StaffFaqFacade;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.faq.FaqResponse;
import com.mangoboss.storage.faq.FaqCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/faq")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffFaqController {

    private final StaffFaqFacade staffFaqFacade;

    @GetMapping
    public ListWrapperResponse<FaqResponse> getFaqs(@RequestParam(value = "category", defaultValue = "ALL") FaqCategory category) {
        return ListWrapperResponse.of(staffFaqFacade.getFaqs(category));
    }
}