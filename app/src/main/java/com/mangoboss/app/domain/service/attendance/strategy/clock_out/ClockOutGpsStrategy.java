package com.mangoboss.app.domain.service.attendance.strategy.clock_out;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.storage.store.AttendanceMethod;
import org.springframework.stereotype.Component;

import com.mangoboss.app.dto.attendance.clock_out.ClockOutGpsRequest;
import com.mangoboss.storage.store.StoreEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ClockOutGpsStrategy implements ClockOutStrategy<ClockOutGpsRequest> {

    private final AttendanceService attendanceService;

    @Override
    public boolean supports(StoreEntity store, AttendanceMethod method) {
        return store.getAttendanceMethod() == AttendanceMethod.GPS && method == AttendanceMethod.GPS;
    }

    @Override
    public Class<ClockOutGpsRequest> getRequestType() {
        return ClockOutGpsRequest.class;
    }

    @Override
    public void validateInternal(StoreEntity store, ClockOutGpsRequest request) {
        attendanceService.validateGps(store, request.latitude(), request.longitude(), request.locationFetchedAt());
    }
}