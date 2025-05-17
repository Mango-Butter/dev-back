package com.mangoboss.app.api.controller.document;

import com.mangoboss.app.api.facade.document.BossDocumentFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.document.request.RequiredDocumentCreateRequest;
import com.mangoboss.app.dto.document.response.RequiredDocumentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boss/stores/{storeId}/documents")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossDocumentController {

    private final BossDocumentFacade bossDocumentFacade;

    @PostMapping("/required")
    public ListWrapperResponse<RequiredDocumentResponse> updateRequiredDocuments(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                                                 @PathVariable final Long storeId,
                                                                                 @RequestBody @Valid final List<RequiredDocumentCreateRequest> requestList) {
        final Long bossId = userDetails.getUserId();
        return ListWrapperResponse.of(bossDocumentFacade.updateRequiredDocuments(storeId, bossId, requestList));
    }

    @GetMapping("/required")
    public ListWrapperResponse<RequiredDocumentResponse> getRequiredDocuments(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                                              @PathVariable final Long storeId) {
        final Long bossId = userDetails.getUserId();
        return ListWrapperResponse.of(bossDocumentFacade.getRequiredDocuments(storeId, bossId));
    }
}