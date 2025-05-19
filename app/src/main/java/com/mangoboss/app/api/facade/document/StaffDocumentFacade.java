package com.mangoboss.app.api.facade.document;

import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.service.document.DocumentService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.document.request.DocumentUploadRequest;
import com.mangoboss.app.dto.document.response.MyDocumentStatusResponse;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import com.mangoboss.storage.document.DocumentEntity;
import com.mangoboss.storage.document.DocumentType;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffDocumentFacade {

    private final S3FileManager s3FileManager;
    private final DocumentService documentService;
    private final StaffService staffService;

    public void uploadDocument(final Long storeId, final Long userId, final DocumentUploadRequest request) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        documentService.uploadDocument(request.documentData(), request.documentType(), request.expiresAt(), storeId, staff.getId());
    }

    public ViewPreSignedUrlResponse viewDocument(Long storeId, Long userId, Long documentId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final String key = documentService.getFileKeyByIdAndStaffId(staff.getId(), documentId);
        return s3FileManager.generateViewPreSignedUrl(key);
    }

    public DownloadPreSignedUrlResponse downloadDocument(final Long storeId, final Long userId, final Long documentId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final String key = documentService.getFileKeyByIdAndStaffId(staff.getId(), documentId);
        return s3FileManager.generateDownloadPreSignedUrl(key);
    }

    public void deleteDocument(final Long storeId, final Long userId, final Long documentId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final DocumentEntity document = documentService.getByDocumentId(documentId);
        documentService.validateDocumentBelongsToStaff(document.getStaffId(), staff.getId());
        documentService.deleteDocument(document);
    }

    public List<MyDocumentStatusResponse> getMyDocumentStatus(final Long storeId, final Long userId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final List<DocumentEntity> documents = documentService.findAllByStoreIdAndStaffId(storeId, staff.getId());

        final Map<DocumentType, DocumentEntity> typeToDocumentMap = documents.stream()
                .collect(Collectors.toMap(DocumentEntity::getDocumentType, Function.identity()));

        return Arrays.stream(DocumentType.values())
                .map(type -> {
                    final DocumentEntity document = typeToDocumentMap.get(type);
                    final boolean isSubmitted = document != null;
                    final LocalDate expiresAt = isSubmitted ? document.getExpiresAt() : null;
                    final Long documentId = isSubmitted ? document.getId() : null;

                    return MyDocumentStatusResponse.of(type, isSubmitted, expiresAt, documentId);
                })
                .toList();
    }
}
