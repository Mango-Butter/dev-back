package com.mangoboss.admin.dto.dashboard;

import com.mangoboss.storage.store.StoreType;
import lombok.Builder;

@Builder
public record StoreTypeStatisticsResponse(
        StoreType storeType,
        Long storeCount
) {
    public static StoreTypeStatisticsResponse of(final StoreType storeType, Long storeCount) {
        return StoreTypeStatisticsResponse.builder()
                .storeType(storeType)
                .storeCount(storeCount)
                .build();
    }
}