package com.mangoboss.admin.dto.auth.requeset;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RefreshTokenRequest(
	@NotBlank
	String refreshToken
) {
}