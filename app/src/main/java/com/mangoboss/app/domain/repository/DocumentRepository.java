package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.document.DocumentEntity;

public interface DocumentRepository {
    void save(DocumentEntity entity);

    DocumentEntity getByIdAndStaffId(Long id, Long staffId);

    void delete(DocumentEntity entity);
}
