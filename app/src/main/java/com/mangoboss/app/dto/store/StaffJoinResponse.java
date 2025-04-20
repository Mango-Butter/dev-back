package com.mangoboss.app.dto.store;

import com.mangoboss.storage.store.StoreEntity;
import lombok.Builder;

@Builder
public record StaffJoinResponse (
        Long storeId
){
    public static StaffJoinResponse fromEntity(final StoreEntity store){
        return StaffJoinResponse.builder()
                .storeId(store.getId())
                .build();
    }
}
