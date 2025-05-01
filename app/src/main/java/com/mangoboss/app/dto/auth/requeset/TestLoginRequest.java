package com.mangoboss.app.dto.auth.requeset;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TestLoginRequest(
        @NotNull
        Long userId
) {}