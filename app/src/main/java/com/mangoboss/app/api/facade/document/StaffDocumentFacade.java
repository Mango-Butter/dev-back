package com.mangoboss.app.api.facade.document;

import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.service.document.DocumentService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.document.request.DocumentUploadRequest;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffDocumentFacade {

    private final S3FileManager s3FileManager;
    private final DocumentService documentService;
    private final StaffService staffService;

    public void uploadDocument(final Long storeId, final Long staffId, final DocumentUploadRequest request) {
        staffService.getStaffBelongsToStore(storeId, staffId);
        documentService.uploadDocument(request.documentData(), request.documentType(), request.expiresAt(), storeId, staffId);
    }

    public ViewPreSignedUrlResponse viewDocument(Long storeId, Long staffId, Long documentId) {
        staffService.getStaffBelongsToStore(storeId, staffId);
        final String key = documentService.getFileKeyByIdAndStaffId(staffId, documentId);
        return s3FileManager.generateViewPreSignedUrl(key);
    }

    public DownloadPreSignedUrlResponse downloadDocument(final Long storeId, final Long staffId, final Long documentId) {
        staffService.getStaffBelongsToStore(storeId, staffId);
        final String key = documentService.getFileKeyByIdAndStaffId(staffId, documentId);
        return s3FileManager.generateDownloadPreSignedUrl(key);
    }

    public void deleteDocument(final Long storeId, final Long staffId, final Long documentId) {
        staffService.getStaffBelongsToStore(storeId, staffId);
        documentService.deleteDocument(staffId, documentId);
    }
}
