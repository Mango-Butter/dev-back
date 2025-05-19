package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.document.DocumentEntity;
import com.mangoboss.storage.document.DocumentType;

import java.util.List;

public interface DocumentRepository {
    void save(DocumentEntity entity);

    DocumentEntity getByIdAndStaffId(Long id, Long staffId);

    DocumentEntity getById(Long id);

    void delete(DocumentEntity entity);

    List<DocumentEntity> findAllByStoreIdAndDocumentType(Long storeId, DocumentType documentType);

    List<DocumentEntity> findAllByStoreIdAndStaffId(Long storeId, Long staffId);
}
