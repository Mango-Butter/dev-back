package com.mangoboss.app.dto.store.response;

import com.mangoboss.storage.store.StoreEntity;
import lombok.Builder;

@Builder
public record GpsSettingsResponse(
        String address,
        Integer gpsRangeMeters,
        Double gpsLatitude,
        Double gpsLongitude
) {
    public static GpsSettingsResponse fromEntity(final StoreEntity store) {
        return GpsSettingsResponse.builder()
                .address(store.getAddress())
                .gpsRangeMeters(store.getGpsRangeMeters())
                .gpsLatitude(store.getGpsLatitude())
                .gpsLongitude(store.getGpsLongitude())
                .build();
    }
}