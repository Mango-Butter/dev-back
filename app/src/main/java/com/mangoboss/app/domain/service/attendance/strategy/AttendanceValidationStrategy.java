package com.mangoboss.app.domain.service.attendance.strategy;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;

public interface AttendanceValidationStrategy<T extends AttendanceBaseRequest> {

    boolean supports(StoreEntity store, AttendanceMethod method);

    void validateInternal(StoreEntity store, T request);

    Class<T> getRequestType();

    @SuppressWarnings("unchecked")
    default void executionValidate(StoreEntity store, AttendanceBaseRequest request) {
        if (!getRequestType().isInstance(request)) {
            throw new CustomException(CustomErrorInfo.INVALID_ATTENDANCE_REQUEST_TYPE);
        }
        validateInternal(store, (T) request);
    }
}