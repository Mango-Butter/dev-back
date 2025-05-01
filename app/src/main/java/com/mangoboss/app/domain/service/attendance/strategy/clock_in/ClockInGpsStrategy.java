package com.mangoboss.app.domain.service.attendance.strategy.clock_in;

import com.mangoboss.app.domain.service.attendance.AttendanceService;
import com.mangoboss.app.dto.attendance.clock_in.ClockInGpsRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClockInGpsStrategy implements ClockInStrategy<ClockInGpsRequest> {

    private final AttendanceService attendanceService;

    @Override
    public boolean supports(StoreEntity store, AttendanceMethod method) {
        return store.getAttendanceMethod() == AttendanceMethod.GPS && method == AttendanceMethod.GPS;
    }

    @Override
    public Class<ClockInGpsRequest> getRequestType() {
        return ClockInGpsRequest.class;
    }

    @Override
    public void validateInternal(StoreEntity store, ClockInGpsRequest request) {
        attendanceService.validateGps(store, request.latitude(), request.longitude(), request.locationFetchedAt());
    }
}