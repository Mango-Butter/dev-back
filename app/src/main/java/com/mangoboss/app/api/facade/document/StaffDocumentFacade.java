package com.mangoboss.app.api.facade.document;

import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.service.document.DocumentService;
import com.mangoboss.app.domain.service.document.RequiredDocumentService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.document.request.DocumentUploadRequest;
import com.mangoboss.app.dto.document.response.DocumentStatusResponse;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import com.mangoboss.storage.document.DocumentEntity;
import com.mangoboss.storage.document.DocumentType;
import com.mangoboss.storage.document.RequiredDocumentEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StaffDocumentFacade {

    private final S3FileManager s3FileManager;
    private final DocumentService documentService;
    private final StaffService staffService;
    private final RequiredDocumentService requiredDocumentService;

    public void uploadDocument(final Long storeId, final Long userId, final DocumentUploadRequest request) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        documentService.uploadDocument(request.documentData(), request.documentType(), request.expiresAt(), storeId, staff.getId());
    }

    public ViewPreSignedUrlResponse viewDocument(final Long storeId, final Long userId, final Long documentId) {
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

    public List<DocumentStatusResponse> getMyDocumentStatus(final Long storeId, final Long userId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);

        final Map<DocumentType, DocumentEntity> documentMap = documentService
                .findAllByStoreIdAndStaffId(storeId, staff.getId())
                .stream()
                .collect(Collectors.toMap(DocumentEntity::getDocumentType, Function.identity()));

        final Set<DocumentType> requiredDocumentTypes = requiredDocumentService.findAllByStoreId(storeId)
                .stream()
                .filter(RequiredDocumentEntity::isRequired)
                .map(RequiredDocumentEntity::getDocumentType)
                .collect(Collectors.toSet());

        return Arrays.stream(DocumentType.values())
                .map(type -> {
                    final DocumentEntity doc = documentMap.get(type);
                    final boolean isSubmitted = doc != null;
                    final boolean isRequired = requiredDocumentTypes.contains(type);
                    final LocalDate expiresAt = isSubmitted ? doc.getExpiresAt() : null;
                    final Long documentId = isSubmitted ? doc.getId() : null;

                    return DocumentStatusResponse.of(type, isSubmitted, isRequired, expiresAt, documentId);
                })
                .toList();
    }

}
