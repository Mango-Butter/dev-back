package com.mangoboss.app.dto.store.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GpsRegisterRequest(
	@NotBlank
	String address,

	@NotNull
	Double latitude,

	@NotNull
	Double longitude,

	@NotNull
	Integer gpsRangeMeters
) {}