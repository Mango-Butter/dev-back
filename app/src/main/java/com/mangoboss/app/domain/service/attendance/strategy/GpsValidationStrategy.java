package com.mangoboss.app.domain.service.attendance.strategy;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.attendance.base.GpsRequest;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class GpsValidationStrategy implements AttendanceValidationStrategy<GpsRequest> {

    private static final long MAX_GPS_TIME_DIFF_SECONDS = 10;

    @Override
    public boolean supports(StoreEntity store, AttendanceMethod method) {
        return store.getAttendanceMethod() == AttendanceMethod.GPS && method == AttendanceMethod.GPS;
    }

    @Override
    public Class<GpsRequest> getRequestType() {
        return GpsRequest.class;
    }

    @Override
    public void validateInternal(StoreEntity store, GpsRequest request) {
        final LocalDateTime fetchedAt = request.locationFetchedAt();
        if (Duration.between(fetchedAt, LocalDateTime.now()).abs().getSeconds() > MAX_GPS_TIME_DIFF_SECONDS) {
            throw new CustomException(CustomErrorInfo.INVALID_GPS_TIME);
        }

        double distance = calculateDistance(store.getGpsLatitude(), store.getGpsLongitude(),
                request.latitude(), request.longitude());

        if (distance > store.getGpsRangeMeters()) {
            throw new CustomException(CustomErrorInfo.GPS_OUT_OF_RANGE);
        }
    }

    private double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        final double R = 6371000.0;
        final double dLat = Math.toRadians(lat2 - lat1);
        final double dLon = Math.toRadians(lon2 - lon1);
        final double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        final double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}