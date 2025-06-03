package com.mangoboss.app.dto.store.response;

import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import lombok.Builder;

@Builder
public record StaffStoreItemResponse(
        Long storeId,
        String storeName,
        String address,
        String storeType,
        AttendanceMethod attendanceMethod
) {
    public static StaffStoreItemResponse of(final StoreEntity store) {
        return StaffStoreItemResponse.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .address(store.getAddress())
                .storeType(store.getStoreType().name())
                .attendanceMethod(store.getAttendanceMethod())
                .build();
    }
}