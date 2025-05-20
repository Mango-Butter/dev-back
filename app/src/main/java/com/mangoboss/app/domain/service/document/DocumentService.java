package com.mangoboss.app.domain.service.document;

import com.mangoboss.app.common.constant.ContentType;
import com.mangoboss.app.common.constant.S3FileType;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.security.EncryptedFileDecoder;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.repository.DocumentRepository;
import com.mangoboss.storage.document.DocumentEntity;
import com.mangoboss.storage.document.DocumentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final S3FileManager s3FileManager;
    private final EncryptedFileDecoder encryptedFileDecoder;

    @Transactional
    public void uploadDocument(final String documentData, final DocumentType documentType, final LocalDate expiresAt,
                               final Long storeId, final Long staffId) {

        final EncryptedFileDecoder.DecodedFile decoded = encryptedFileDecoder.decode(documentData);
        final String mimeType = decoded.mimeType();
        final byte[] fileBytes = decoded.fileBytes();

        final String extension = ContentType.getExtensionByMimeType(mimeType);
        final String fileKey = s3FileManager.generateFileKey(S3FileType.DOCUMENT, extension);

        s3FileManager.upload(fileBytes, fileKey, mimeType);

        final DocumentEntity entity = DocumentEntity.create(staffId, storeId, documentType, fileKey, mimeType, expiresAt);

        documentRepository.save(entity);
    }

    public String getFileKeyByIdAndStaffId(final Long staffId, final Long documentId) {
        final DocumentEntity document = documentRepository.getByIdAndStaffId(documentId, staffId);
        return document.getFileKey();
    }

    public DocumentEntity getByDocumentId(final Long documentId) {
        return documentRepository.getById(documentId);
    }

    @Transactional
    public void deleteDocument(final DocumentEntity document) {
        s3FileManager.deleteFile(document.getFileKey());
        documentRepository.delete(document);
    }

    public List<DocumentEntity> findAllByStoreIdAndType(final Long storeId, final DocumentType type) {
        return documentRepository.findAllByStoreIdAndDocumentType(storeId, type);
    }

    public void validateDocumentBelongsToStaff(final Long documentStaffId, final Long staffId) {
        if (!documentStaffId.equals(staffId)) {
            throw new CustomException(CustomErrorInfo.DOCUMENT_NOT_BELONG_TO_STAFF);
        }
    }

    public List<DocumentEntity> findAllByStoreIdAndStaffId(final Long storeId, final Long staffId) {
        return documentRepository.findAllByStoreIdAndStaffId(storeId, staffId);
    }

    public void validateNotAlreadyUploaded(final Long staffId, final DocumentType documentType) {
        if (documentRepository.existsByStaffIdAndDocumentType(staffId, documentType)) {
            throw new CustomException(CustomErrorInfo.DOCUMENT_ALREADY_UPLOADED);
        }
    }
}