package com.mangoboss.app.dto.store.response;

import lombok.Builder;

@Builder
public record QrCodeResponse(
	String qrCode,
	Long storeId
) {
	public static QrCodeResponse of(final Long storeId, final String qrCode) {
		return QrCodeResponse.builder()
				.qrCode(qrCode)
				.storeId(storeId)
				.build();
	}
}