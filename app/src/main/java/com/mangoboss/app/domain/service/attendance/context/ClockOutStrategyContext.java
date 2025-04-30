package com.mangoboss.app.domain.service.attendance.context;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.service.attendance.strategy.clock_out.ClockOutStrategy;
import com.mangoboss.app.dto.attendance.base.ClockOutBaseRequest;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 퇴근 전략을 attendanceMethod + DTO 타입 기준으로 찾아주는 Context 클래스.
 * 전략 내부 캐스팅이 안전하도록 타입을 resolve에서 보장한다.
 */
@Component
@RequiredArgsConstructor
public class ClockOutStrategyContext {

    private final List<ClockOutStrategy<? extends ClockOutBaseRequest>> strategies;

    @SuppressWarnings("unchecked") // getRequestType().isInstance(request)로 타입 체크 완료
    public <T extends ClockOutBaseRequest> ClockOutStrategy<T> resolve(StoreEntity store, T request) {
        return (ClockOutStrategy<T>) strategies.stream()
                .filter(s -> s.supports(store, request.attendanceMethod())) // 현재 전략이 요청(method)을 처리할 수 있는지 판단 ex. AttendanceMethod.BOTH, AttendanceMethod.QR 등과 비교
                .filter(s -> s.getRequestType().isInstance(request)) // /이 전략이 처리할 수 있는 Request 타입을 알려줌 ex. ClockOutQrStrategy는 ClockOutQrRequest.class 반환
                .findFirst()
                .orElseThrow(() -> new CustomException(CustomErrorInfo.INVALID_ATTENDANCE_REQUEST_TYPE));
    }
}