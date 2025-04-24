package com.mangoboss.app.api.controller.store;

import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.store.request.StoreUpdateRequest;
import com.mangoboss.app.dto.store.response.StoreInfoResponse;
import com.mangoboss.app.dto.store.response.StoreListResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.mangoboss.app.api.facade.store.BossStoreFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.store.request.BusinessNumberRequest;
import com.mangoboss.app.dto.store.request.StoreCreateRequest;
import com.mangoboss.app.dto.store.response.StoreCreateResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boss/stores")
@PreAuthorize("hasRole('BOSS')")
public class BossStoreController {
    private final BossStoreFacade bossStoreFacade;

    @PostMapping
    public StoreCreateResponse createStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @RequestBody @Valid StoreCreateRequest request) {
        final Long userId = userDetails.getUserId();
        return bossStoreFacade.createStore(userId, request);
    }

    @PostMapping("/validations/business-number")
    public void validateBusinessNumber(@RequestBody @Valid BusinessNumberRequest request) {
        bossStoreFacade.validateBusinessNumber(request.businessNumber());
    }

    @GetMapping
    public ListWrapperResponse<StoreListResponse> getMyStores(@AuthenticationPrincipal CustomUserDetails userDetails) {
        final Long userId = userDetails.getUserId();
        return bossStoreFacade.getMyStores(userId);
    }

    @GetMapping("/{storeId}/store-info")
    public StoreInfoResponse getStoreInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable final Long storeId) {
        final Long userId = userDetails.getUserId();
        return bossStoreFacade.getStoreInfo(userId, storeId);
    }

    @PutMapping("/{storeId}/store-info")
    public void updateStoreInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @PathVariable final Long storeId,
                                @RequestBody @Valid StoreUpdateRequest request) {
        final Long userId = userDetails.getUserId();
        bossStoreFacade.updateStoreInfo(userId, storeId, request);
    }
}