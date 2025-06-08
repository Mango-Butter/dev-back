package com.mangoboss.app.domain.service.attendance.strategy;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.attendance.base.QrRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import org.springframework.stereotype.Component;

@Component
public class QrValidationStrategy implements AttendanceValidationStrategy<QrRequest> {

    @Override
    public boolean supports(StoreEntity store, AttendanceMethod method) {
        return store.getAttendanceMethod() == AttendanceMethod.QR && method == AttendanceMethod.QR;
    }

    @Override
    public Class<QrRequest> getRequestType() {
        return QrRequest.class;
    }

    @Override
    public void validateInternal(StoreEntity store, QrRequest request) {
        if (!store.getQrCode().equals(request.qrCode())) {
            throw new CustomException(CustomErrorInfo.INVALID_QR_CODE);
        }
    }
}