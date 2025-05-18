package com.mangoboss.storage.document;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentJpaRepository extends JpaRepository<DocumentEntity, Long> {
    Optional<DocumentEntity> findByIdAndStaffId(Long id, Long staffId);
    List<DocumentEntity> findAllByStoreIdAndDocumentType(Long storeId, DocumentType documentType);

}