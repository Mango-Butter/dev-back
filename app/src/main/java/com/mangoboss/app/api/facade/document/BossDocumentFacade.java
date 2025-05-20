package com.mangoboss.app.api.facade.document;

import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.service.document.DocumentService;
import com.mangoboss.app.domain.service.document.RequiredDocumentService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.document.request.RequiredDocumentCreateRequest;
import com.mangoboss.app.dto.document.response.DocumentStatusResponse;
import com.mangoboss.app.dto.document.response.RequiredDocumentResponse;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import com.mangoboss.storage.document.DocumentEntity;
import com.mangoboss.app.dto.document.response.StaffDocumentStatusResponse;
import com.mangoboss.storage.document.DocumentType;
import com.mangoboss.storage.document.RequiredDocumentEntity;
import com.mangoboss.storage.store.StoreEntity;
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
public class BossDocumentFacade {

    private final DocumentService documentService;
    private final RequiredDocumentService requiredDocumentService;
    private final StoreService storeService;
    private final S3FileManager s3FileManager;
    private final StaffService staffService;

    public List<RequiredDocumentResponse> updateRequiredDocuments(final Long storeId, final Long bossId, final List<RequiredDocumentCreateRequest> requests) {
        final StoreEntity store = storeService.isBossOfStore(storeId, bossId);
        requiredDocumentService.updateRequiredDocuments(store, requests);

        return requiredDocumentService.findAllByStoreId(store.getId()).stream()
                .map(RequiredDocumentResponse::fromEntity)
                .toList();
    }

    public List<RequiredDocumentResponse> getRequiredDocuments(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        return requiredDocumentService.findAllByStoreId(storeId).stream()
                .map(RequiredDocumentResponse::fromEntity)
                .toList();
    }

    public List<StaffDocumentStatusResponse> getStaffDocumentStatusByDocumentType(final Long storeId, final Long bossId, final DocumentType documentType) {
        storeService.isBossOfStore(storeId, bossId);

        final Map<Long, DocumentEntity> documentMap = documentService.findAllByStoreIdAndType(storeId, documentType)
                .stream()
                .collect(Collectors.toMap(DocumentEntity::getStaffId, Function.identity()));

        return staffService.getStaffsForStore(storeId).stream()
                .map(staff -> {
                    final Long staffId = staff.getId();
                    final String name = staff.getName();
                    final DocumentEntity document = documentMap.get(staffId);

                    return StaffDocumentStatusResponse.of(
                            staffId,
                            name,
                            document != null,
                            document != null ? document.getId() : null
                    );
                })
                .toList();
    }

    public ViewPreSignedUrlResponse viewDocument(final Long storeId, final Long bossId, final Long documentId) {
        storeService.isBossOfStore(storeId, bossId);
        final DocumentEntity document = documentService.getByDocumentId(documentId);
        return s3FileManager.generateViewPreSignedUrl(document.getFileKey());
    }

    public DownloadPreSignedUrlResponse downloadDocument(final Long storeId, final Long bossId, final Long documentId) {
        storeService.isBossOfStore(storeId, bossId);
        final DocumentEntity document = documentService.getByDocumentId(documentId);
        return s3FileManager.generateDownloadPreSignedUrl(document.getFileKey());
    }

    public void deleteDocument(final Long storeId, final Long bossId, final Long documentId) {
        storeService.isBossOfStore(storeId, bossId);
        final DocumentEntity document = documentService.getByDocumentId(documentId);
        documentService.deleteDocument(document);
    }

    public List<DocumentStatusResponse> getDocumentStatusByStaff(final Long storeId, final Long bossId, final Long staffId) {
        storeService.isBossOfStore(storeId, bossId);
        staffService.validateStaffBelongsToStore(storeId, staffId);

        final Map<DocumentType, DocumentEntity> documentMap = documentService
                .findAllByStoreIdAndStaffId(storeId, staffId)
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