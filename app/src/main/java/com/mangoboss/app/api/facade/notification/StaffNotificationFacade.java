package com.mangoboss.app.api.facade.notification;

import com.mangoboss.app.domain.service.notification.NotificationService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.notification.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffNotificationFacade {

    private final NotificationService notificationService;
    private final StaffService staffService;

    public List<NotificationResponse> getNotificationsByUserAndStore(final Long storeId, final Long userId) {
        staffService.getVerifiedStaff(userId, storeId);
        return notificationService.getNotificationsByUserAndStore(userId, storeId).stream()
                .map(NotificationResponse::fromEntity)
                .toList();
    }
}
