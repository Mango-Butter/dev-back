package com.mangoboss.app.dto.store.request;

import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.store.StoreType;
import com.mangoboss.storage.user.UserEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StoreCreateRequest(
        @NotBlank
        String storeName,

        @NotBlank
        String address,

        @NotBlank
        String businessNumber,

        @NotNull
        StoreType storeType,

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
                this.storeName(),
                this.address(),
                this.businessNumber,
                this.storeType(),
                inviteCode,
                this.gps().latitude(),
                this.gps().longitude(),
                qrCode
        );
    }
}