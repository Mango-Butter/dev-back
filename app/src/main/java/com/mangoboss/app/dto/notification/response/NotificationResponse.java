package com.mangoboss.app.dto.notification.response;

import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        Long id,
        String title,
        String content,
        String imageUrl,
        String clickUrl,
        NotificationType type,
        LocalDateTime createdAt
) {
    public static NotificationResponse fromEntity(final NotificationEntity entity) {
        return NotificationResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageUrl(entity.getImageUrl())
                .clickUrl(entity.getClickUrl())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}