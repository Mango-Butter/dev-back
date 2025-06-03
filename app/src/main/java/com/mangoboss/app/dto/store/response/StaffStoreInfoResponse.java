package com.mangoboss.app.dto.store.response;

import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.AttendanceMethod;
import com.mangoboss.storage.store.StoreEntity;
import lombok.Builder;

@Builder
public record StaffStoreInfoResponse(
        StaffSimpleResponse staff,
        Long storeId,
        String storeName,
        String address,
        String storeType,
        AttendanceMethod attendanceMethod
) {
    public static StaffStoreInfoResponse of(final StaffEntity staff, final StoreEntity store) {
        return StaffStoreInfoResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staff))
                .storeId(store.getId())
                .storeName(store.getName())
                .address(store.getAddress())
                .storeType(store.getStoreType().name())
                .attendanceMethod(store.getAttendanceMethod())
                .build();
    }
}
