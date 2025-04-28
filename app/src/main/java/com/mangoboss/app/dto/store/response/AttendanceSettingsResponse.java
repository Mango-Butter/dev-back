package com.mangoboss.app.dto.store.response;

import com.mangoboss.storage.store.StoreEntity;
import lombok.Builder;

@Builder
public record AttendanceSettingsResponse(
		Boolean useQr,
		Boolean useGps
) {
	public static AttendanceSettingsResponse fromEntity(final StoreEntity store) {
		return AttendanceSettingsResponse.builder()
				.useQr(store.getUseQr())
				.useGps(store.getUseGps())
				.build();
	}
}