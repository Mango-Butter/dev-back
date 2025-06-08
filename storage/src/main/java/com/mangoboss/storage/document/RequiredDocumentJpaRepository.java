package com.mangoboss.storage.document;

import com.mangoboss.storage.store.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequiredDocumentJpaRepository extends JpaRepository<RequiredDocumentEntity, Long> {
    Optional<RequiredDocumentEntity> findByStoreAndDocumentType(StoreEntity store, DocumentType documentType);

    List<RequiredDocumentEntity> findAllByStoreId(Long storeId);
}