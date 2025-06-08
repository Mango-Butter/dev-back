package com.mangoboss.storage.notification;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long storeId;

    @Column(nullable = false, length = 5000)
    private String targetToken;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(length = 2083)
    private String imageUrl;

    @Column( nullable = false)
    private String clickUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SendStatus sendStatus;

    @Column(nullable = false)
    private int retryCount;

    @Builder
    private NotificationEntity(
            final Long userId,
            final Long storeId,
            final String targetToken,
            final String title,
            final String content,
            final String imageUrl,
            final String clickUrl,
            final NotificationType type,
            final SendStatus sendStatus,
            final int retryCount
    ) {
        this.userId = userId;
        this.storeId = storeId;
        this.targetToken = targetToken;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.clickUrl = clickUrl;
        this.type = type;
        this.sendStatus = sendStatus;
        this.retryCount = retryCount;
    }

    public static NotificationEntity create(
            final Long userId,
            final Long storeId,
            final String title,
            final String content,
            final String imageUrl,
            final String clickUrl,
            final NotificationType type,
            final String targetToken
    ) {
        return NotificationEntity.builder()
                .userId(userId)
                .storeId(storeId)
                .targetToken(targetToken)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .clickUrl(clickUrl)
                .type(type)
                .sendStatus(SendStatus.PENDING)
                .retryCount(0)
                .build();
    }

    public void markFailure() {
        this.sendStatus = SendStatus.FAILED;
        this.retryCount++;
    }

    public void markSuccess() {
        this.sendStatus = SendStatus.COMPLETED;
    }
}