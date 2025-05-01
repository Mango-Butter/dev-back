package com.mangoboss.app.domain.service.attendance.strategy.clock_in;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.attendance.clock_in.ClockInQrRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClockInQrStrategy implements ClockInStrategy<ClockInQrRequest> {

    @Override
    public boolean supports(StoreEntity store, AttendanceMethod method) {
        return store.getAttendanceMethod() == AttendanceMethod.QR && method == AttendanceMethod.QR;
    }

    @Override
    public Class<ClockInQrRequest> getRequestType() {
        return ClockInQrRequest.class;
    }

    @Override
    public void validateInternal(StoreEntity store, ClockInQrRequest request) {
        if (!store.getQrCode().equals(request.qrCode())) {
            throw new CustomException(CustomErrorInfo.INVALID_QR_CODE);
        }
    }
}