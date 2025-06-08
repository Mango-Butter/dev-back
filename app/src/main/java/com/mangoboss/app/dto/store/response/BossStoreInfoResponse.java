package com.mangoboss.app.dto.store.response;

import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.store.StoreType;
import lombok.Builder;

@Builder
public record BossStoreInfoResponse(
        Long storeId,
        String storeName,
        String businessNumber,
        StoreType storeType,
        String address,
        String inviteCode,
        Integer overtimeLimit
) {
    public static BossStoreInfoResponse fromEntity(final StoreEntity store) {
        return BossStoreInfoResponse.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .businessNumber(store.getBusinessNumber())
                .storeType(store.getStoreType())
                .address(store.getAddress())
                .inviteCode(store.getInviteCode())
                .overtimeLimit(store.getOvertimeLimit())
                .build();
    }
}