package com.mangoboss.app.api.facade.notification;

import com.mangoboss.app.domain.service.notification.NotificationService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.notification.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BossNotificationFacade {

    private final NotificationService notificationService;
    private final StoreService storeService;

    public List<NotificationResponse> getNotificationsByUserAndStore(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        return notificationService.getNotificationsByUserAndStore(bossId, storeId).stream()
                .map(NotificationResponse::fromEntity)
                .toList();
    }
}
