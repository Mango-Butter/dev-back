package com.mangoboss.app.dto.auth.response;

import lombok.Builder;

@Builder
public record JwtResponse(
        String grantType,
        String accessToken,
        String refreshToken
) {

}
