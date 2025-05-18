package com.mangoboss.app.api.controller.document;

import com.mangoboss.app.api.facade.document.BossDocumentFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.document.request.RequiredDocumentCreateRequest;
import com.mangoboss.app.dto.document.response.RequiredDocumentResponse;
import com.mangoboss.app.dto.document.response.StaffDocumentStatusResponse;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import com.mangoboss.storage.document.DocumentType;
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

    @GetMapping("/types/{documentType}/staffs")
    public ListWrapperResponse<StaffDocumentStatusResponse> getStaffDocumentStatusByDocumentType(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                                                                 @PathVariable final Long storeId,
                                                                                                 @PathVariable final DocumentType documentType) {
        final Long bossId = userDetails.getUserId();
        return ListWrapperResponse.of(bossDocumentFacade.getStaffDocumentStatusByDocumentType(storeId, bossId, documentType));
    }

    @GetMapping("/{documentId}/view-url")
    public ViewPreSignedUrlResponse viewDocument(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                 @PathVariable final Long storeId,
                                                 @PathVariable Long documentId) {
        final Long bossId = userDetails.getUserId();
        return bossDocumentFacade.viewDocument(storeId, bossId, documentId);
    }

    @GetMapping("/{documentId}/download-url")
    public DownloadPreSignedUrlResponse downloadDocument(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                         @PathVariable final Long storeId,
                                                         @PathVariable Long documentId) {
        final Long bossId = userDetails.getUserId();
        return bossDocumentFacade.downloadDocument(storeId, bossId, documentId);
    }

    @DeleteMapping("/{documentId}")
    public void deleteDocument(@AuthenticationPrincipal final CustomUserDetails userDetails,
                               @PathVariable final Long storeId,
                               @PathVariable final Long documentId) {
        final Long bossId = userDetails.getUserId();
        bossDocumentFacade.deleteDocument(storeId, bossId, documentId);
    }

}