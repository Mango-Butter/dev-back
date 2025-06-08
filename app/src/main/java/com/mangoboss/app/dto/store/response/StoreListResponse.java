package com.mangoboss.app.dto.store.response;

import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.store.StoreType;
import lombok.Builder;

@Builder
public record StoreListResponse(
        Long storeId,
        String storeName,
        String businessNumber,
        StoreType storeType,
        String address,
        String inviteCode
) {
    public static StoreListResponse fromEntity(final StoreEntity entity) {
        return StoreListResponse.builder()
                .storeId(entity.getId())
                .storeName(entity.getName())
                .businessNumber(entity.getBusinessNumber())
                .storeType(entity.getStoreType())
                .address(entity.getAddress())
                .inviteCode(entity.getInviteCode())
                .build();
    }
}
