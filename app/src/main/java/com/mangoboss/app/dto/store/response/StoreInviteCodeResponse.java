package com.mangoboss.app.dto.store.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record StoreInviteCodeResponse(
        @NotBlank
        String inviteCode
) {
    public static StoreInviteCodeResponse of(final String inviteCode) {
        return StoreInviteCodeResponse.builder()
                .inviteCode(inviteCode)
                .build();
    }
}