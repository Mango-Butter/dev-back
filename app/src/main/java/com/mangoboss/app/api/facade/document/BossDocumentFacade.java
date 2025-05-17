package com.mangoboss.app.api.facade.document;

import com.mangoboss.app.domain.service.document.RequiredDocumentService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.document.request.RequiredDocumentCreateRequest;
import com.mangoboss.app.dto.document.response.RequiredDocumentResponse;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BossDocumentFacade {

    private final RequiredDocumentService requiredDocumentService;
    private final StoreService storeService;

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
}