package com.mangoboss.app.domain.service.attendance.context;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.service.attendance.strategy.AttendanceValidationStrategy;
import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AttendanceStrategyContext {

    private final List<AttendanceValidationStrategy<? extends AttendanceBaseRequest>> strategies;

    public void validate(StoreEntity store, AttendanceBaseRequest request) {
        strategies.stream()
                .filter(s -> s.supports(store, request.attendanceMethod()))
                .filter(s -> s.getRequestType().isInstance(request))
                .findFirst()
                .orElseThrow(() -> new CustomException(CustomErrorInfo.INVALID_ATTENDANCE_REQUEST_TYPE))
                .executionValidate(store, request);
    }
}