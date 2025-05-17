package com.mangoboss.app.domain.service.document;

import com.mangoboss.app.common.constant.ContentType;
import com.mangoboss.app.common.constant.S3FileType;
import com.mangoboss.app.common.security.EncryptedFileDecoder;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.repository.DocumentRepository;
import com.mangoboss.storage.document.DocumentEntity;
import com.mangoboss.storage.document.DocumentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final S3FileManager s3FileManager;
    private final EncryptedFileDecoder encryptedFileDecoder;

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

    public void deleteDocument(final Long staffId, final Long documentId) {
        final DocumentEntity document = documentRepository.getByIdAndStaffId(documentId, staffId);
        s3FileManager.deleteFile(document.getFileKey());
        documentRepository.delete(document);
    }
}