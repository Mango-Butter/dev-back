package com.mangoboss.app.api.facade.document;

import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.service.document.DocumentService;
import com.mangoboss.app.domain.service.document.RequiredDocumentService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.document.request.RequiredDocumentCreateRequest;
import com.mangoboss.app.dto.document.response.RequiredDocumentResponse;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import com.mangoboss.storage.document.DocumentEntity;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BossDocumentFacade {

    private final DocumentService documentService;
    private final RequiredDocumentService requiredDocumentService;
    private final StoreService storeService;
    private final S3FileManager s3FileManager;

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

    public ViewPreSignedUrlResponse viewDocument(Long storeId, Long bossId, Long documentId) {
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
}