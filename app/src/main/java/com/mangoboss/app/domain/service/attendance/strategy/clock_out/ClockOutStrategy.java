package com.mangoboss.app.domain.service.attendance.strategy.clock_out;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.attendance.base.ClockOutBaseRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;

public interface ClockOutStrategy<T extends ClockOutBaseRequest> {

    boolean supports(StoreEntity store, AttendanceMethod method);
    void validateInternal(StoreEntity store, T request);
    Class<T> getRequestType();

    @SuppressWarnings("unchecked") // getRequestType().isInstance(...) 검증 후이므로 안전한 캐스팅
    default void validate(StoreEntity store, ClockOutBaseRequest request) { // 타입이 맞는지 검사하고, 안전하게 validateInternal()을 호출
        if (!getRequestType().isInstance(request)) {
            throw new CustomException(CustomErrorInfo.INVALID_ATTENDANCE_REQUEST_TYPE);
        }
        validateInternal(store, (T) request); // 실제 전략별 유효성 검사 로직 담당 (예: QR 코드 비교, GPS 거리 체크 등)
    }
}