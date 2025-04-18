package com.mangoboss.app.dto.user.response;

import lombok.Builder;

@Builder
public record JwtResponse(
        String grantType,
        String accessToken,
        String refreshToken
) {

}
