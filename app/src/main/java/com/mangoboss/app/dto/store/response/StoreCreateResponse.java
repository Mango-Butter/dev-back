package com.mangoboss.app.dto.store.response;

import com.mangoboss.storage.store.StoreEntity;

import lombok.Builder;

@Builder
public record StoreCreateResponse(
	Long storeId
) {
	public static StoreCreateResponse fromEntity(final StoreEntity entity) {
		return StoreCreateResponse.builder()
			.storeId(entity.getId())
			.build();
	}
}
