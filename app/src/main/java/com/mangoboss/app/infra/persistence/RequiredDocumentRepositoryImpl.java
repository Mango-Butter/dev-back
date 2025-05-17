package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.RequiredDocumentRepository;
import com.mangoboss.storage.document.DocumentType;
import com.mangoboss.storage.document.RequiredDocumentEntity;
import com.mangoboss.storage.document.RequiredDocumentJpaRepository;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RequiredDocumentRepositoryImpl implements RequiredDocumentRepository {
    private final RequiredDocumentJpaRepository requiredDocumentJpaRepository;

    @Override
    public RequiredDocumentEntity getByStoreAndDocumentType(final StoreEntity store, final DocumentType documentType) {
        return requiredDocumentJpaRepository.findByStoreAndDocumentType(store, documentType)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.REQUIRED_DOCUMENT_NOT_FOUND));
    }

    @Override
    public List<RequiredDocumentEntity> findAllByStoreId(Long storeId) {
        return requiredDocumentJpaRepository.findAllByStoreId(storeId);
    }

    @Override
    public RequiredDocumentEntity save(RequiredDocumentEntity entity) {
        return requiredDocumentJpaRepository.save(entity);
    }
}