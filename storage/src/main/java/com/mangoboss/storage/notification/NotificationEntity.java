package com.mangoboss.storage.notification;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "notification",
        indexes = {
                @Index(name = "idx_notification_type_meta", columnList = "type, meta_id"),
                @Index(name = "idx_notification_sendstatus_retry", columnList = "send_status, retry_count"),
                @Index(name = "idx_notification_user_store", columnList = "user_id, store_id")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    private Long userId;

    private Long storeId;

    @Column(length = 5000)
    private String targetToken;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(length = 2083)
    private String imageUrl;

    @Column(nullable = false)
    private String clickUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SendStatus sendStatus;

    @Column(nullable = false)
    private int retryCount;

    private Long metaId;

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
            final int retryCount,
            final Long metaId
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
        this.metaId = metaId;
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
                .metaId(null)
                .build();
    }

    public static NotificationEntity createWithMetaId(
            final Long userId,
            final Long storeId,
            final String title,
            final String content,
            final String imageUrl,
            final String clickUrl,
            final NotificationType type,
            final String targetToken,
            final Long metaId
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
                .metaId(metaId)
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