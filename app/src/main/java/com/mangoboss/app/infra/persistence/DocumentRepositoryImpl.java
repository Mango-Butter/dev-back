package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.DocumentRepository;
import com.mangoboss.storage.document.DocumentEntity;
import com.mangoboss.storage.document.DocumentJpaRepository;
import com.mangoboss.storage.document.DocumentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DocumentRepositoryImpl implements DocumentRepository {
    private final DocumentJpaRepository documentJpaRepository;

    @Override
    public DocumentEntity save(DocumentEntity entity) {
        return documentJpaRepository.save(entity);
    }

    @Override
    public DocumentEntity getByIdAndStaffId(final Long id, final Long staffId) {
        return documentJpaRepository.findByIdAndStaffId(id, staffId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.DOCUMENT_NOT_FOUND));
    }

    @Override
    public DocumentEntity getById(final Long id) {
        return documentJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.DOCUMENT_NOT_FOUND));
    }

    @Override
    public void delete(final DocumentEntity entity) {
        documentJpaRepository.delete(entity);
    }

    @Override
    public List<DocumentEntity> findAllByStoreIdAndDocumentType(final Long storeId, final DocumentType documentType) {
        return documentJpaRepository.findAllByStoreIdAndDocumentType(storeId, documentType);
    }

    @Override
    public List<DocumentEntity> findAllByStoreIdAndStaffId(final Long storeId, final Long staffId) {
        return documentJpaRepository.findAllByStoreIdAndStaffId(storeId, staffId);
    }

    @Override
    public boolean existsByStaffIdAndDocumentType(final Long staffId, final DocumentType documentType) {
        return documentJpaRepository.existsByStaffIdAndDocumentType(staffId, documentType);
    }
}