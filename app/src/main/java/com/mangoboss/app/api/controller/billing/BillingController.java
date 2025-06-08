package com.mangoboss.app.api.controller.billing;

import com.mangoboss.app.api.facade.billing.BillingFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.billing.BillingRegisterRequest;
import com.mangoboss.app.dto.subscription.response.BillingCustomerKeyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@PreAuthorize("hasRole('BOSS')")
public class BillingController {

    private final BillingFacade billingFacade;

    @PostMapping("/billing/issue")
    public void issueBillingKey(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @RequestBody @Valid BillingRegisterRequest request) {
        final Long userId = userDetails.getUserId();
        billingFacade.issueBillingKey(userId, request);
    }

    @GetMapping("/customer-key")
    public BillingCustomerKeyResponse getCustomerKey(@AuthenticationPrincipal CustomUserDetails userDetails) {
        final Long userId = userDetails.getUserId();
        return billingFacade.getOrCreateCustomerKey(userId);
    }
}