package com.mangoboss.app.domain.service.attendance.strategy;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.attendance.request.AttendanceBothRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BothValidationStrategyTest {

    private BothValidationStrategy bothValidationStrategy;
    private StoreEntity store;

    @BeforeEach
    void setUp() {
        bothValidationStrategy = new BothValidationStrategy();
        store = StoreEntity.builder()
                .qrCode("VALID_QR")
                .gpsLatitude(37.123)
                .gpsLongitude(127.123)
                .gpsRangeMeters(100)
                .attendanceMethod(AttendanceMethod.BOTH)
                .build();
    }

    @Test
    void 유효한_요청이면_검증을_통과한다() {
        var request = new AttendanceBothRequest(AttendanceMethod.BOTH, 1L, "VALID_QR", 37.123, 127.123, LocalDateTime.now());
        assertDoesNotThrow(() -> bothValidationStrategy.executionValidate(store, request));
    }

    @Test
    void 잘못된_qr이면_검증에서_예외가_발생한다() {
        var request = new AttendanceBothRequest(AttendanceMethod.BOTH, 1L, "WRONG_QR", 37.123, 127.123, LocalDateTime.now());
        var ex = assertThrows(CustomException.class, () -> bothValidationStrategy.executionValidate(store, request));
        assertEquals(CustomErrorInfo.INVALID_QR_CODE, ex.getErrorCode());
    }

    @Test
    void gps_범위를_벗어나면_예외가_발생한다() {
        var request = new AttendanceBothRequest(AttendanceMethod.BOTH, 1L, "VALID_QR", 36.0, 126.0, LocalDateTime.now());
        var ex = assertThrows(CustomException.class, () -> bothValidationStrategy.executionValidate(store, request));
        assertEquals(CustomErrorInfo.GPS_OUT_OF_RANGE, ex.getErrorCode());
    }

    @Test
    void gps_시간차이가_10초를_초과하면_예외가_발생한다() {
        var request = new AttendanceBothRequest(AttendanceMethod.BOTH, 1L, "VALID_QR", 37.123, 127.123, LocalDateTime.now().minusSeconds(20));
        var ex = assertThrows(CustomException.class, () -> bothValidationStrategy.executionValidate(store, request));
        assertEquals(CustomErrorInfo.INVALID_GPS_TIME, ex.getErrorCode());
    }

    @Test
    void 지원하는_가게_설정과_출결방식이면_true를_반환한다() {
        assertTrue(bothValidationStrategy.supports(store, AttendanceMethod.BOTH));
    }

    @Test
    void 출결방식이나_가게설정이_다르면_false를_반환한다() {
        assertFalse(bothValidationStrategy.supports(store, AttendanceMethod.QR));
    }
}