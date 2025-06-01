package com.mangoboss.storage.notification;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "device_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceTokenEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_token_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String tokenValue;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    private DeviceTokenEntity(
            final Long userId,
            final String tokenValue,
            final boolean isDeleted
    ) {
        this.userId = userId;
        this.tokenValue = tokenValue;
        this.isDeleted = isDeleted;
    }

    public static DeviceTokenEntity create(final Long userId, final String tokenValue) {
        return DeviceTokenEntity.builder()
                .userId(userId)
                .tokenValue(tokenValue)
                .isDeleted(false)
                .build();
    }

    public void delete() {
        this.isDeleted = true;
    }
}