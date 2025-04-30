package com.mangoboss.app.domain.service.attendance.strategy.clock_in;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.dto.attendance.clock_in.ClockInBothRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClockInBothStrategy implements ClockInStrategy<ClockInBothRequest> {

    private final AttendanceService attendanceService;

    @Override
    public boolean supports(StoreEntity store, String method) {
        return store.getAttendanceMethod() == AttendanceMethod.BOTH && method.equalsIgnoreCase("BOTH");
    }

    @Override
    public Class<ClockInBothRequest> getRequestType() {
        return ClockInBothRequest.class;
    }

    @Override
    public void validateInternal(StoreEntity store, ClockInBothRequest request) {
        attendanceService.validateGps(store, request.latitude(), request.longitude(), request.locationFetchedAt());
        attendanceService.validateQr(store, request.qrCode());
    }
}