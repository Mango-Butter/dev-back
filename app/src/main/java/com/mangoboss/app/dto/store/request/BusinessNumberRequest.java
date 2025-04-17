package com.mangoboss.app.dto.store.request;

import jakarta.validation.constraints.NotBlank;

public record BusinessNumberRequest(
	@NotBlank
	String businessNumber
) {}