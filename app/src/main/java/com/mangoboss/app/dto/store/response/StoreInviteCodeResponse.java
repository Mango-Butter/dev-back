package com.mangoboss.app.dto.store.response;

import lombok.Builder;

@Builder
public record StoreInviteCodeResponse(
        String inviteCode
) {
    public static StoreInviteCodeResponse of(final String inviteCode) {
        return StoreInviteCodeResponse.builder()
                .inviteCode(inviteCode)
                .build();
    }
}