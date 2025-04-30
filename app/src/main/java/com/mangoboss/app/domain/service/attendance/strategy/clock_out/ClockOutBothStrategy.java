package com.mangoboss.app.domain.service.attendance.strategy.clock_out;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.storage.store.AttendanceMethod;
import org.springframework.stereotype.Component;

import com.mangoboss.app.dto.attendance.clock_out.ClockOutBothRequest;
import com.mangoboss.storage.store.StoreEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClockOutBothStrategy implements ClockOutStrategy<ClockOutBothRequest> {

    private final AttendanceService attendanceService;

    @Override
    public boolean supports(StoreEntity store, String method) {
        return store.getAttendanceMethod() == AttendanceMethod.BOTH && method.equalsIgnoreCase("BOTH");
    }

    @Override
    public Class<ClockOutBothRequest> getRequestType() {
        return ClockOutBothRequest.class;
    }

    @Override
    public void validateInternal(StoreEntity store, ClockOutBothRequest request) {
        attendanceService.validateGps(store, request.latitude(), request.longitude(), request.locationFetchedAt());
        attendanceService.validateQr(store, request.qrCode());
    }
}
