package com.mangoboss.app.api.controller.document;

import com.mangoboss.app.api.facade.document.StaffDocumentFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.document.request.DocumentUploadRequest;
import com.mangoboss.app.dto.document.response.DocumentStatusResponse;
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
        final Long userId = userDetails.getUserId();
        staffDocumentFacade.uploadDocument(storeId, userId, request);
    }

    @GetMapping("/{documentId}/view-url")
    public ViewPreSignedUrlResponse viewDocument(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                 @PathVariable final Long storeId,
                                                 @PathVariable Long documentId) {
        final Long userId = userDetails.getUserId();
        return staffDocumentFacade.viewDocument(storeId, userId, documentId);
    }

    @GetMapping("/{documentId}/download-url")
    public DownloadPreSignedUrlResponse downloadDocument(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                         @PathVariable final Long storeId,
                                                         @PathVariable Long documentId) {
        final Long userId = userDetails.getUserId();
        return staffDocumentFacade.downloadDocument(storeId, userId, documentId);
    }

    @DeleteMapping("/{documentId}")
    public void deleteDocument(@AuthenticationPrincipal final CustomUserDetails userDetails,
                               @PathVariable final Long storeId,
                               @PathVariable final Long documentId) {
        final Long userId = userDetails.getUserId();
        staffDocumentFacade.deleteDocument(storeId, userId, documentId);
    }

    @GetMapping("")
    public ListWrapperResponse<DocumentStatusResponse> getMyDocumentStatus(@AuthenticationPrincipal final CustomUserDetails userDetails,
                                                                           @PathVariable final Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(staffDocumentFacade.getMyDocumentStatus(storeId, userId));
    }
}