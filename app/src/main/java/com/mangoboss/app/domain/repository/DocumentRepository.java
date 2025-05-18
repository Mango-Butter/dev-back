package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.document.DocumentEntity;

public interface DocumentRepository {
    void save(DocumentEntity entity);

    DocumentEntity getByIdAndStaffId(Long id, Long staffId);

    DocumentEntity getById(Long id);

    void delete(DocumentEntity entity);
}
