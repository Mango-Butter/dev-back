package com.mangoboss.app.dto.auth.requeset;

import com.mangoboss.storage.user.Role;

import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
	@NotBlank
	Role role
) {}
