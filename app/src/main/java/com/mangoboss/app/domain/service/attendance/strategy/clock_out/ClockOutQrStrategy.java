package com.mangoboss.app.domain.service.attendance.strategy.clock_out;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.storage.store.AttendanceMethod;
import org.springframework.stereotype.Component;

import com.mangoboss.app.dto.attendance.clock_out.ClockOutQrRequest;
import com.mangoboss.storage.store.StoreEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClockOutQrStrategy implements ClockOutStrategy<ClockOutQrRequest> {

    private final AttendanceService attendanceService;

    @Override
    public boolean supports(StoreEntity store, AttendanceMethod method) {
        return store.getAttendanceMethod() == AttendanceMethod.QR && method == AttendanceMethod.QR;
    }

    @Override
    public Class<ClockOutQrRequest> getRequestType() {
        return ClockOutQrRequest.class;
    }

    @Override
    public void validateInternal(StoreEntity store, ClockOutQrRequest request) {
        attendanceService.validateQr(store, request.qrCode());
    }
}