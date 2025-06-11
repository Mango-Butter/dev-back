package com.mangoboss.app.domain.service.document;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.security.EncryptedFileDecoder;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.repository.DocumentRepository;
import com.mangoboss.storage.document.DocumentEntity;
import com.mangoboss.storage.document.DocumentType;
import com.mangoboss.storage.metadata.S3FileType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private S3FileManager s3FileManager;

    @Mock
    private EncryptedFileDecoder encryptedFileDecoder;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void 문서를_업로드_할_수_있다() {
        // given
        String documentData = "encrypted-data";
        DocumentType documentType = DocumentType.ID_CARD;
        LocalDate expiresAt = LocalDate.now().plusDays(30);
        Long storeId = 1L;
        Long staffId = 1L;

        EncryptedFileDecoder.DecodedFile decodedFile = new EncryptedFileDecoder.DecodedFile("application/pdf", new byte[]{1, 2, 3});
        when(encryptedFileDecoder.decode(documentData)).thenReturn(decodedFile);

        when(s3FileManager.generateFileKey(S3FileType.DOCUMENT, "pdf"))
                .thenReturn("test-file-key");

        DocumentEntity savedDocument = DocumentEntity.create(staffId, storeId, documentType, "test-file-key", "application/pdf", expiresAt);
        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(savedDocument);

        // when
        documentService.uploadDocument(documentData, documentType, expiresAt, storeId, staffId);

        // then
        verify(s3FileManager, times(1)).upload(any(), anyString(), eq("application/pdf"));
        verify(documentRepository, times(1)).save(any(DocumentEntity.class));
    }

    @Test
    void 이미_업로드된_문서면_에러를_던진다() {
        // given
        Long staffId = 1L;
        DocumentType documentType = DocumentType.ID_CARD;

        when(documentRepository.existsByStaffIdAndDocumentType(staffId, documentType)).thenReturn(true);

        // when & then
        Assertions.assertThatThrownBy(() -> documentService.validateNotAlreadyUploaded(staffId, documentType))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.DOCUMENT_ALREADY_UPLOADED.getMessage());
    }

    @Test
    void 본인_문서가_아닐_경우_에러를_던진다() {
        // given
        Long documentStaffId = 1L;
        Long staffId = 2L;

        // when & then
        Assertions.assertThatThrownBy(() -> documentService.validateDocumentBelongsToStaff(documentStaffId, staffId))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.DOCUMENT_NOT_BELONG_TO_STAFF.getMessage());
    }

    @Test
    void 문서를_삭제할_수_있다() {
        // given
        DocumentEntity document = mock(DocumentEntity.class);
        when(document.getFileKey()).thenReturn("file-key");

        // when
        documentService.deleteDocument(document);

        // then
        verify(s3FileManager).deleteFileFromPrivateBucket("file-key");
        verify(documentRepository).delete(document);
    }

    @Test
    void 매장과_알바생ID로_문서목록을_조회할_수_있다() {
        // given
        Long storeId = 1L, staffId = 2L;
        List<DocumentEntity> documents = List.of(mock(DocumentEntity.class));
        when(documentRepository.findAllByStoreIdAndStaffId(storeId, staffId)).thenReturn(documents);

        // when
        List<DocumentEntity> result = documentService.findAllByStoreIdAndStaffId(storeId, staffId);

        // then
        Assertions.assertThat(result).isEqualTo(documents);
    }

    @Test
    void 매장과_문서타입으로_문서목록을_조회할_수_있다() {
        // given
        Long storeId = 1L;
        DocumentType type = DocumentType.ID_CARD;
        List<DocumentEntity> documents = List.of(mock(DocumentEntity.class));
        when(documentRepository.findAllByStoreIdAndDocumentType(storeId, type)).thenReturn(documents);

        // when
        List<DocumentEntity> result = documentService.findAllByStoreIdAndType(storeId, type);

        // then
        Assertions.assertThat(result).isEqualTo(documents);
    }

    @Test
    void 문서ID로_단일_문서를_조회할_수_있다() {
        // given
        Long documentId = 1L;
        DocumentEntity document = mock(DocumentEntity.class);
        when(documentRepository.getById(documentId)).thenReturn(document);

        // when
        DocumentEntity result = documentService.getByDocumentId(documentId);

        // then
        Assertions.assertThat(result).isEqualTo(document);
    }

    @Test
    void 특정_알바생의_문서로부터_파일키를_조회할_수_있다() {
        // given
        Long staffId = 1L;
        Long documentId = 2L;
        DocumentEntity document = mock(DocumentEntity.class);
        when(document.getFileKey()).thenReturn("s3-key");
        when(documentRepository.getByIdAndStaffId(documentId, staffId)).thenReturn(document);

        // when
        String result = documentService.getFileKeyByIdAndStaffId(staffId, documentId);

        // then
        Assertions.assertThat(result).isEqualTo("s3-key");
    }

    @Test
    void 아직_업로드되지_않은_문서는_예외없이_통과한다() {
        // given
        Long staffId = 1L;
        DocumentType documentType = DocumentType.ID_CARD;

        when(documentRepository.existsByStaffIdAndDocumentType(staffId, documentType)).thenReturn(false);

        // when & then
        Assertions.assertThatCode(() ->
                documentService.validateNotAlreadyUploaded(staffId, documentType)
        ).doesNotThrowAnyException();
    }

    @Test
    void 문서가_본인_문서이면_예외없이_통과한다() {
        // given
        Long staffId = 1L;
        Long documentStaffId = 1L;

        // when & then
        Assertions.assertThatCode(() ->
                documentService.validateDocumentBelongsToStaff(documentStaffId, staffId)
        ).doesNotThrowAnyException();
    }

    @Test
    void 지원하지_않는_파일형식이면_에러를_던진다() {
        // given
        String documentData = "invalid";
        DocumentType documentType = DocumentType.ID_CARD;
        LocalDate expiresAt = LocalDate.now();
        Long storeId = 1L;
        Long staffId = 1L;

        EncryptedFileDecoder.DecodedFile decodedFile =
                new EncryptedFileDecoder.DecodedFile("application/unknown", new byte[]{1, 2, 3});
        when(encryptedFileDecoder.decode(documentData)).thenReturn(decodedFile);

        // when & then
        Assertions.assertThatThrownBy(() ->
                        documentService.uploadDocument(documentData, documentType, expiresAt, storeId, staffId)
                ).isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.UNSUPPORTED_FILE_TYPE.getMessage());
    }
}