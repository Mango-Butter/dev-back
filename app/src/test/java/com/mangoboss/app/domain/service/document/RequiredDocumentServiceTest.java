package com.mangoboss.app.domain.service.document;

import com.mangoboss.app.domain.repository.RequiredDocumentRepository;
import com.mangoboss.app.dto.document.request.RequiredDocumentCreateRequest;
import com.mangoboss.storage.document.DocumentType;
import com.mangoboss.storage.document.RequiredDocumentEntity;
import com.mangoboss.storage.store.StoreEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequiredDocumentServiceTest {

    @Mock
    private RequiredDocumentRepository requiredDocumentRepository;

    @InjectMocks
    private RequiredDocumentService requiredDocumentService;

    @Test
    void 필수_서류_상태를_업데이트할_수_있다() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        DocumentType type = DocumentType.BANK_ACCOUNT;
        RequiredDocumentCreateRequest request = new RequiredDocumentCreateRequest(type, true);

        RequiredDocumentEntity entity = mock(RequiredDocumentEntity.class);
        when(requiredDocumentRepository.getByStoreAndDocumentType(store, type)).thenReturn(entity);

        // when
        requiredDocumentService.updateRequiredDocuments(store, List.of(request));

        // then
        verify(entity).updateRequiredStatus(true);
        verify(requiredDocumentRepository).save(entity);
    }

    @Test
    void 가게ID로_모든_필수서류를_조회할_수_있다() {
        // given
        Long storeId = 1L;
        List<RequiredDocumentEntity> expectedList = List.of(mock(RequiredDocumentEntity.class));
        when(requiredDocumentRepository.findAllByStoreId(storeId)).thenReturn(expectedList);

        // when
        List<RequiredDocumentEntity> result = requiredDocumentService.findAllByStoreId(storeId);

        // then
        assertEquals(expectedList, result);
    }

    @Test
    void 모든_DocumentType에_대해_초기화된_필수서류를_저장할_수_있다() {
        // given
        StoreEntity store = mock(StoreEntity.class);

        // when
        requiredDocumentService.initRequiredDocuments(store);

        // then
        verify(requiredDocumentRepository, times(DocumentType.values().length)).save(any());
    }
}