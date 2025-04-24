package com.mangoboss.app.domain.service.store;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StoreRepository;
import com.mangoboss.app.dto.store.request.StoreUpdateRequest;
import com.mangoboss.storage.store.StoreEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    @Test
    void 매장_정보를_정상적으로_수정할_수_있다() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        Long storeId = 10L;
        String newAddress = "경기도 수원시 팔달구 우만동";
        String newChatLink = "http://open.kakao.com/abc123";
        StoreUpdateRequest request = new StoreUpdateRequest(newAddress, newChatLink);
        when(storeRepository.getById(storeId)).thenReturn(store);

        // when
        storeService.updateStoreInfo(storeId, request);

        // then
        verify(store).updateInfo(newAddress, newChatLink);
    }

    @Test
    void 존재하지_않는_매장이면_STORE_NOT_FOUND_예외를_던진다() {
        // given
        Long storeId = 99L;
        StoreUpdateRequest request = new StoreUpdateRequest("주소", "링크");
        when(storeRepository.getById(storeId))
                .thenThrow(new CustomException(CustomErrorInfo.STORE_NOT_FOUND));

        // when
        // then
        Assertions.assertThatThrownBy(() -> storeService.updateStoreInfo(storeId, request))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.STORE_NOT_FOUND.getMessage());
    }
}
