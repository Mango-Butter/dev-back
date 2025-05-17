package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.document.DocumentType;
import com.mangoboss.storage.document.RequiredDocumentEntity;
import com.mangoboss.storage.store.StoreEntity;

import java.util.List;

public interface RequiredDocumentRepository {

    List<RequiredDocumentEntity> findAllByStoreId(Long storeId);

    RequiredDocumentEntity save(RequiredDocumentEntity entity);

    RequiredDocumentEntity getByStoreAndDocumentType(StoreEntity store, DocumentType documentType);
}