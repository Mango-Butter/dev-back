package com.mangoboss.app.domain.service.attendance.context;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.service.attendance.strategy.AttendanceValidationStrategy;
import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AttendanceStrategyContextTest {

    private AttendanceStrategyContext context;
    private AttendanceValidationStrategy<TestRequest> mockStrategy;

    @BeforeEach
    void setUp() {
        mockStrategy = mock(AttendanceValidationStrategy.class);
        context = new AttendanceStrategyContext(List.of(mockStrategy));
    }

    @Test
    void 전략이_정상적으로_선택되면_검증이_수행된다() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        TestRequest request = new TestRequest();

        when(mockStrategy.supports(store, request.attendanceMethod())).thenReturn(true);
        when(mockStrategy.getRequestType()).thenReturn(TestRequest.class);

        // when
        context.validate(store, request);

        // then
        verify(mockStrategy).executionValidate(store, request);
    }

    @Test
    void 지원하는_전략이_없으면_예외가_발생한다() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        TestRequest request = new TestRequest();

        when(mockStrategy.supports(store, request.attendanceMethod())).thenReturn(false);
        when(mockStrategy.getRequestType()).thenReturn(TestRequest.class);

        // when & then
        assertThatThrownBy(() -> context.validate(store, request))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode())
                        .isEqualTo(CustomErrorInfo.INVALID_ATTENDANCE_REQUEST_TYPE));

        verify(mockStrategy, never()).executionValidate(any(), any());
    }

    @Test
    void request_타입이_전략에서_요구하는_타입과_다르면_예외가_발생한다() {
        // given
        StoreEntity store = mock(StoreEntity.class);
        WrongRequest request = new WrongRequest();

        when(mockStrategy.supports(store, request.attendanceMethod())).thenReturn(true);
        when(mockStrategy.getRequestType()).thenReturn(TestRequest.class); // 타입 불일치

        // when & then
        assertThatThrownBy(() -> context.validate(store, request))
                .isInstanceOf(CustomException.class)
                .satisfies(e -> assertThat(((CustomException) e).getErrorCode())
                        .isEqualTo(CustomErrorInfo.INVALID_ATTENDANCE_REQUEST_TYPE));

        verify(mockStrategy, never()).executionValidate(any(), any());
    }

    // 테스트용 request 구현체들
    private static class TestRequest implements AttendanceBaseRequest {
        @Override
        public AttendanceMethod attendanceMethod() {
            return AttendanceMethod.GPS;
        }

        @Override
        public Long scheduleId() {
            return 1L;
        }
    }

    private static class WrongRequest implements AttendanceBaseRequest {
        @Override
        public AttendanceMethod attendanceMethod() {
            return AttendanceMethod.GPS;
        }

        @Override
        public Long scheduleId() {
            return 2L;
        }
    }
}