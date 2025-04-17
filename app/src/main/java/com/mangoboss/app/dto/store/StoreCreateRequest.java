package com.mangoboss.app.dto.store;

import com.mangoboss.storage.store.StoreType;

public record StoreCreateRequest(
	String storeName,
	String businessNumber,
	StoreType storeType,
	String address,
	Gps gps
) {
	public record Gps(
		Double longitude,
		Double latitude
	) {}
}