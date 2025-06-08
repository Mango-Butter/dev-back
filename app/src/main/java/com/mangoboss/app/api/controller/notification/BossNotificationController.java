package com.mangoboss.app.api.controller.notification;

import com.mangoboss.app.api.facade.notification.BossNotificationFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.notification.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boss/stores/{storeId}/notifications")
@PreAuthorize("hasRole('BOSS')")
public class BossNotificationController {

    private final BossNotificationFacade bossNotificationFacade;

    @PreAuthorize("hasRole('BOSS')")
    @GetMapping
    public ListWrapperResponse<NotificationResponse> getNotificationsByUserAndStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                    @PathVariable final Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossNotificationFacade.getNotificationsByUserAndStore(storeId, userId));
    }
}