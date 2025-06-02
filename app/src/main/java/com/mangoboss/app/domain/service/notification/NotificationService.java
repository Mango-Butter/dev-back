package com.mangoboss.app.domain.service.notification;

import com.mangoboss.app.domain.repository.DeviceTokenRepository;
import com.mangoboss.app.domain.repository.NotificationRepository;
import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Value("${frontend-url}")
    private String frontendUrl;

    private final NotificationRepository notificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    public void sendContractSignRequestNotification(final Long staffUserId, final Long storeId) {
        final String contractClickUrl = frontendUrl + "/staff/document?type=contract";

        final List<String> targetTokens = deviceTokenRepository.findActiveTokensByUserId(staffUserId);
            for (String token : targetTokens) {
                final NotificationEntity notification = NotificationEntity.create(
                        staffUserId,
                        storeId,
                        "근로계약서 서명 요청",
                        "사장님이 근로계약서 서명 요청을 보냈습니다.\n근로계약서를 확인하고 서명해주세요.",
                        "https://ajou.ac.kr/_attach/ajou/editor-image/2025/02/dczBjMULjPNcmuVTyGfUjEgUaZ.jpg",
                        contractClickUrl,
                        NotificationType.CONTRACT,
                        token
                );
                notificationRepository.save(notification);
            }
    }

    public List<NotificationEntity> getNotificationsByUserAndStore(final Long userId, final Long storeId) {
        return notificationRepository.findByUserIdAndStoreIdOrderByCreatedAtDesc(userId, storeId);
    }

}
