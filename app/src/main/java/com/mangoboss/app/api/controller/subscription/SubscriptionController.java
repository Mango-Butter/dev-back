package com.mangoboss.app.api.controller.subscription;

import com.mangoboss.app.api.facade.subscription.SubscriptionFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.subscription.request.SubscriptionCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
@PreAuthorize("hasRole('BOSS')")
public class SubscriptionController {

    private final SubscriptionFacade subscriptionFacade;

    @PostMapping
    public void createOrReplaceSubscription(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @RequestBody @Valid SubscriptionCreateRequest request) {
        final Long userId = userDetails.getUserId();
        subscriptionFacade.createOrReplaceSubscription(userId, request);
    }
}