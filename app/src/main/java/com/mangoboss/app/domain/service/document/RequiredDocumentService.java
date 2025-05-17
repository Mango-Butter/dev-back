package com.mangoboss.app.domain.service.document;

import com.mangoboss.app.domain.repository.RequiredDocumentRepository;
import com.mangoboss.app.dto.document.request.RequiredDocumentCreateRequest;
import com.mangoboss.storage.document.DocumentType;
import com.mangoboss.storage.document.RequiredDocumentEntity;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequiredDocumentService {

    private final RequiredDocumentRepository requiredDocumentRepository;

    @Transactional
    public void updateRequiredDocuments(final StoreEntity store, final List<RequiredDocumentCreateRequest> requests) {
        for (RequiredDocumentCreateRequest request : requests) {
            final DocumentType documentType = request.documentType();
            final boolean isRequired = request.isRequired();

            final RequiredDocumentEntity existing = requiredDocumentRepository
                    .getByStoreAndDocumentType(store, documentType);

            existing.updateRequiredStatus(isRequired);
            requiredDocumentRepository.save(existing);
        }
    }

    @Transactional(readOnly = true)
    public List<RequiredDocumentEntity> findAllByStoreId(final Long storeId) {
        return requiredDocumentRepository.findAllByStoreId(storeId);
    }

    @Transactional
    public void initRequiredDocuments(final StoreEntity store) {
        for (DocumentType type : DocumentType.values()) {
            final RequiredDocumentEntity requiredDoc = RequiredDocumentEntity.init(store, type);
            requiredDocumentRepository.save(requiredDoc);
        }
    }
}
