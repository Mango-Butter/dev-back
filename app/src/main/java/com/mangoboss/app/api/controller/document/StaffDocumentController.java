package com.mangoboss.app.api.controller.document;

import com.mangoboss.app.api.facade.document.StaffDocumentFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.document.request.DocumentUploadRequest;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/stores/{storeId}/documents")
@RequiredArgsConstructor
public class StaffDocumentController {

    private final StaffDocumentFacade staffDocumentFacade;

    @PostMapping("/upload")
    public void uploadDocument(@PathVariable final Long storeId,
                               @AuthenticationPrincipal final CustomUserDetails userDetails,
                               @RequestBody @Valid DocumentUploadRequest request) {
        final Long staffId = userDetails.getUserId();
        staffDocumentFacade.uploadDocument(storeId, staffId, request);
    }

    @GetMapping("/{documentId}/view-url")
    public ViewPreSignedUrlResponse viewDocument(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                 @PathVariable final Long storeId,
                                                 @PathVariable Long documentId) {
        final Long staffId = userDetails.getUserId();
        return staffDocumentFacade.viewDocument(storeId, staffId, documentId);
    }

    @GetMapping("/{documentId}/download-url")
    public DownloadPreSignedUrlResponse downloadDocument(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                         @PathVariable final Long storeId,
                                                         @PathVariable Long documentId) {
        final Long staffId = userDetails.getUserId();
        return staffDocumentFacade.downloadDocument(storeId, staffId, documentId);
    }

    @DeleteMapping("/{documentId}")
    public void deleteDocument(@AuthenticationPrincipal final CustomUserDetails userDetails,
                               @PathVariable final Long storeId,
                               @PathVariable final Long documentId) {
        final Long staffId = userDetails.getUserId();
        staffDocumentFacade.deleteDocument(storeId, staffId, documentId);
    }
}