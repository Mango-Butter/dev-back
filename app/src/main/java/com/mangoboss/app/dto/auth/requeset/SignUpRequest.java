package com.mangoboss.app.dto.auth.requeset;

import com.mangoboss.storage.user.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
	@NotNull
	Role role
) {}
