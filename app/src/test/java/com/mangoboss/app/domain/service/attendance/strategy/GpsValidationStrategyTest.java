package com.mangoboss.app.domain.service.attendance.strategy;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.app.dto.attendance.request.AttendanceGpsRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GpsValidationStrategyTest {

    private final GpsValidationStrategy gpsValidationStrategy = new GpsValidationStrategy();

    private final StoreEntity store = StoreEntity.builder()
            .attendanceMethod(AttendanceMethod.GPS)
            .gpsLatitude(37.1234)
            .gpsLongitude(127.1234)
            .gpsRangeMeters(100)
            .qrCode("QR")
            .build();

    @Test
    void gps_범위_내_정상시간이면_예외_없음() {
        AttendanceGpsRequest request = new AttendanceGpsRequest(
                AttendanceMethod.GPS,
                1L,
                37.1234, 127.1234,
                LocalDateTime.now()
        );

        assertDoesNotThrow(() -> gpsValidationStrategy.executionValidate(store, request));
    }

    @Test
    void gps_범위_초과시_예외() {
        AttendanceGpsRequest request = new AttendanceGpsRequest(
                AttendanceMethod.GPS,
                1L,
                36.0, 126.0, // 거리 매우 멀 때
                LocalDateTime.now()
        );

        CustomException ex = assertThrows(CustomException.class, () ->
                gpsValidationStrategy.executionValidate(store, request));

        assertEquals(CustomErrorInfo.GPS_OUT_OF_RANGE, ex.getCustomErrorInfo());
    }

    @Test
    void gps_시간_지연되면_예외() {
        AttendanceGpsRequest request = new AttendanceGpsRequest(
                AttendanceMethod.GPS,
                1L,
                37.1234, 127.1234,
                LocalDateTime.now().minusSeconds(20)
        );

        CustomException ex = assertThrows(CustomException.class, () ->
                gpsValidationStrategy.executionValidate(store, request));

        assertEquals(CustomErrorInfo.INVALID_GPS_TIME, ex.getCustomErrorInfo());
    }

    @Test
    void 지원하지않는_request_type이면_예외발생() {
        AttendanceBaseRequest wrongRequest = new AttendanceBaseRequest() {
            @Override
            public AttendanceMethod attendanceMethod() {
                return AttendanceMethod.GPS;
            }

            @Override
            public Long scheduleId() {
                return 1L;
            }
        };

        CustomException ex = assertThrows(CustomException.class,
                () -> gpsValidationStrategy.executionValidate(store, wrongRequest));

        assertEquals(CustomErrorInfo.INVALID_ATTENDANCE_REQUEST_TYPE, ex.getCustomErrorInfo());
    }

    @Test
    void gps방식과_설정이_일치하면_supports는_true를_반환한다() {
        assertTrue(gpsValidationStrategy.supports(store, AttendanceMethod.GPS));
    }

    @Test
    void gps방식과_설정이_일치하지_않으면_supports는_false를_반환한다() {
        assertFalse(gpsValidationStrategy.supports(store, AttendanceMethod.QR));
    }
}