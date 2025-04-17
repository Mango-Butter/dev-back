package com.mangoboss.app.dto.store;

import lombok.Builder;

@Builder
public record StoreCreateResponse(
	Long storeId
) {}