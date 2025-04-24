package com.mangoboss.app.dto.store.response;

import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.store.StoreType;
import lombok.Builder;

@Builder
public record StoreInfoResponse(
        String storeName,
        String businessNumber,
        StoreType storeType,
        String address,
        String inviteCode,
        String chatLink
) {
    public static StoreInfoResponse fromEntity(final StoreEntity store) {
        return StoreInfoResponse.builder()
                .storeName(store.getName())
                .businessNumber(store.getBusinessNumber())
                .storeType(store.getStoreType())
                .address(store.getAddress())
                .inviteCode(store.getInviteCode())
                .chatLink(store.getChatLink())
                .build();
    }
}