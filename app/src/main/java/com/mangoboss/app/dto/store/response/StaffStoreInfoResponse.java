package com.mangoboss.app.dto.store.response;

import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import lombok.Builder;

@Builder
public record StaffStoreInfoResponse(
        Long storeId,
        String storeName,
        String address,
        String storeType,
        AttendanceMethod attendanceMethod
) {
    public static StaffStoreInfoResponse fromEntity(final StoreEntity store) {
        return StaffStoreInfoResponse.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .address(store.getAddress())
                .storeType(store.getStoreType().name())
                .attendanceMethod(store.getAttendanceMethod())
                .build();
    }
}