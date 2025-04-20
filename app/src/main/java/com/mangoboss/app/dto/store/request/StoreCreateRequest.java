package com.mangoboss.app.dto.store.request;

import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.store.StoreType;
import com.mangoboss.storage.user.UserEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Time;

public record StoreCreateRequest(
        @NotBlank
        String name,

        @NotBlank
        String address,

        @NotBlank
        String businessNumber,

        @NotNull
        StoreType storeType,

        String chatLink,

        Time workingTimeUnit,

        @NotNull
        Gps gps
) {
    public record Gps(
            @NotNull
            Double longitude,

            @NotNull
            Double latitude
    ) {
    }

    public StoreEntity toEntity(final UserEntity boss, final String inviteCode, final String qrCode) {
        return StoreEntity.create(
                boss,
                this.name(),
                this.address(),
                this.businessNumber,
                this.storeType(),
                inviteCode,
                this.chatLink,
                this.workingTimeUnit,
                this.gps().latitude(),
                this.gps().longitude(),
                qrCode
        );
    }
}