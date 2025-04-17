package com.mangoboss.app.dto.store.request;

import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.store.StoreType;
import com.mangoboss.storage.user.UserEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StoreCreateRequest(
	@NotBlank
	String name,

	@NotBlank
	String businessNumber,

	@NotNull
	StoreType storeType,

	@NotBlank
	String address,

	@NotNull
	Gps gps
) {
	public record Gps(
		@NotNull
		Double longitude,

		@NotNull
		Double latitude
	) {}

	public StoreEntity toEntity(final UserEntity boss, final String inviteCode, final String qrCode) {
		return StoreEntity.create(
			boss,
			this.name(),
			this.address(),
			this.storeType(),
			this.businessNumber(),
			inviteCode,
			this.gps().latitude(),
			this.gps().longitude(),
			qrCode
		);
	}
}