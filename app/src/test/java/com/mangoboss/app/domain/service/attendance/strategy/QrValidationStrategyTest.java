package com.mangoboss.app.domain.service.attendance.strategy;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.attendance.request.AttendanceQrRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QrValidationStrategyTest {

    private QrValidationStrategy qrValidationStrategy;
    private StoreEntity store;

    @BeforeEach
    void setUp() {
        qrValidationStrategy = new QrValidationStrategy();
        store = StoreEntity.builder()
                .qrCode("VALID_QR")
                .attendanceMethod(AttendanceMethod.QR)
                .build();
    }

    @Test
    void 올바른_qr이면_통과() {
        var request = new AttendanceQrRequest(AttendanceMethod.QR, 1L, "VALID_QR");

        assertDoesNotThrow(() -> qrValidationStrategy.executionValidate(store, request));
    }

    @Test
    void 잘못된_qr이면_예외() {
        var request = new AttendanceQrRequest(AttendanceMethod.QR, 1L, "WRONG_QR");

        CustomException ex = assertThrows(CustomException.class, () ->
                qrValidationStrategy.executionValidate(store, request)
        );

        assertEquals(CustomErrorInfo.INVALID_QR_CODE, ex.getErrorCode());
    }

    @Test
    void qr방식과_설정이_일치하면_supports는_true를_반환한다() {
        assertTrue(qrValidationStrategy.supports(store, AttendanceMethod.QR));
    }

    @Test
    void qr방식과_설정이_일치하지_않으면_supports는_false를_반환한다() {
        assertFalse(qrValidationStrategy.supports(store, AttendanceMethod.GPS));
    }
}
