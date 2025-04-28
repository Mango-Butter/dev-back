package com.mangoboss.app.domain.service.store;

import com.mangoboss.app.ExternalBusinessApiClient;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StoreRepository;
import com.mangoboss.app.dto.store.request.GpsRegisterRequest;
import com.mangoboss.app.dto.store.request.StoreUpdateRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.store.StoreType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
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
        String invalidBusinessNumber = "invalid";
        when(externalBusinessApiClient.checkBusinessNumberValid(invalidBusinessNumber)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> storeService.validateBusinessNumber(invalidBusinessNumber))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.INVALID_BUSINESS_NUMBER.getMessage());
    }

    @Test
    void 이미_등록된_사업자등록번호면_DUPLICATE_BUSINESS_NUMBER_예외_발생() {
        // given
        String duplicateBusinessNumber = "1234567890";
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
        assertThat(newCode).isNotBlank();
    }

    @Test
    void 출퇴근_방식_설정에_성공한다() {
        // given
        Long storeId = 1L;
        AttendanceMethod method = AttendanceMethod.QR;
        StoreEntity store = mock(StoreEntity.class);
        when(storeRepository.getById(storeId)).thenReturn(store);
        when(store.updateAttendanceMethod(method)).thenReturn(store);

        // when
        StoreEntity updatedStore = storeService.updateAttendanceSettings(storeId, method);

        // then
        verify(store).updateAttendanceMethod(method);
        assertThat(updatedStore).isSameAs(store);
    }

    @Test
    void QR_코드를_재발급할_수_있다() {
        // given
        Long storeId = 1L;
        StoreEntity store = mock(StoreEntity.class);
        when(storeRepository.getById(storeId)).thenReturn(store);

        // when
        String result = storeService.regenerateQrCode(storeId);

        // then
        verify(store).updateQrCode(any());
        assertThat(result).isNotBlank();
    }

    @Test
    void GPS_출퇴근_설정을_변경할_수_있다() {
        // given
        Long storeId = 1L;
        GpsRegisterRequest request = new GpsRegisterRequest("경기도 수원시", 37.1234, 127.5678, 100);
        StoreEntity store = mock(StoreEntity.class);
        when(storeRepository.getById(storeId)).thenReturn(store);
        when(store.updateGpsSettings(request.address(), request.latitude(), request.longitude(), request.gpsRangeMeters())).thenReturn(store);

        // when
        StoreEntity updatedStore = storeService.updateGpsSettings(storeId, request.address(), request.latitude(), request.longitude(), request.gpsRangeMeters());

        // then
        verify(store).updateGpsSettings(request.address(), request.latitude(), request.longitude(), request.gpsRangeMeters());
        assertThat(updatedStore).isSameAs(store);
    }
}
