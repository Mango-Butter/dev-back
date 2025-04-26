package com.mangoboss.app.domain.service.store;

import com.mangoboss.app.ExternalBusinessApiClient;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StoreRepository;
import com.mangoboss.app.dto.store.request.StoreUpdateRequest;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.store.StoreType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ExternalBusinessApiClient externalBusinessApiClient;

    @InjectMocks
    private StoreService storeService;

    @Test
    void 매장_정보를_정상적으로_수정할_수_있다() {
        // given
        Long storeId = 1L;
        StoreEntity store = mock(StoreEntity.class);
        StoreUpdateRequest request = StoreUpdateRequest.builder()
                .address("서울시 강남구")
                .storeType(StoreType.CAFE)
                .build();
        when(storeRepository.getById(storeId)).thenReturn(store);

        // when
        storeService.updateStoreInfo(storeId, request.address(), request.storeType());

        // then
        verify(store).updateInfo(
                request.address(),
                request.storeType()
        );
    }

    @Test
    void 유효하지_않은_사업자등록번호면_INVALID_BUSINESS_NUMBER_예외_발생() {
        // given
        final String invalidBusinessNumber = "invalid";
        when(externalBusinessApiClient.checkBusinessNumberValid(invalidBusinessNumber)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> storeService.validateBusinessNumber(invalidBusinessNumber))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_BUSINESS_NUMBER.getMessage());
    }

    @Test
    void 이미_등록된_사업자등록번호면_DUPLICATE_BUSINESS_NUMBER_예외_발생() {
        // given
        final String duplicateBusinessNumber = "1234567890";
        when(externalBusinessApiClient.checkBusinessNumberValid(duplicateBusinessNumber)).thenReturn(true);
        when(storeRepository.existsByBusinessNumber(duplicateBusinessNumber)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> storeService.validateBusinessNumber(duplicateBusinessNumber))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.DUPLICATE_BUSINESS_NUMBER.getMessage());
    }

    @Test
    void 초대코드를_정상적으로_재발급할_수_있다() {
        // given
        Long storeId = 1L;
        StoreEntity store = mock(StoreEntity.class);
        when(storeRepository.getById(storeId)).thenReturn(store);
        when(storeRepository.existsByInviteCode(anyString())).thenReturn(false); // 중복 없음 가정

        // when
        String newCode = storeService.reissueInviteCode(storeId);

        // then
        verify(store).updateInviteCode(newCode);
        Assertions.assertThat(newCode).isNotBlank();
    }
}
