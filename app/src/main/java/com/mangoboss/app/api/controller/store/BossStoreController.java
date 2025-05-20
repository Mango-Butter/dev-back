package com.mangoboss.app.api.controller.store;

import com.mangoboss.app.dto.store.response.*;
import com.mangoboss.app.dto.store.request.GpsRegisterRequest;
import com.mangoboss.app.dto.store.request.AttendanceSettingsRequest;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.store.request.StoreUpdateRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.mangoboss.app.api.facade.store.BossStoreFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.store.request.BusinessNumberRequest;
import com.mangoboss.app.dto.store.request.StoreCreateRequest;

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
    public ListWrapperResponse<BossStoreInfoResponse> getMyStores(@AuthenticationPrincipal CustomUserDetails userDetails) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossStoreFacade.getMyStores(userId));
    }

    @GetMapping("/{storeId}/store-info")
    public BossStoreInfoResponse getStoreInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
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

    @PostMapping("/{storeId}/reissue-invite-code")
    public StoreInviteCodeResponse reissueInviteCode(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @PathVariable final Long storeId) {
        final Long userId = userDetails.getUserId();
        return bossStoreFacade.reissueInviteCode(userId, storeId);
    }

    @GetMapping("/{storeId}/attendance-settings")
    public AttendanceSettingsResponse getAttendanceSettings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return bossStoreFacade.getAttendanceSettings(storeId, userId);
    }

    @PostMapping("/{storeId}/attendance-settings")
    public AttendanceSettingsResponse updateAttendanceSettings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable Long storeId,
                                                               @RequestBody @Valid AttendanceSettingsRequest request) {
        final Long userId = userDetails.getUserId();
        return bossStoreFacade.updateAttendanceSettings(storeId, userId, request);
    }

    @GetMapping("/{storeId}/attendance-settings/qr")
    public QrCodeResponse getQrSettings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return bossStoreFacade.getQrSettings(storeId, userId);
    }

    @PostMapping("/{storeId}/attendance-settings/qr")
    public QrCodeResponse regenerateQrCode(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return bossStoreFacade.regenerateQrCode(storeId, userId);
    }

    @GetMapping("/{storeId}/attendance-settings/gps")
    public GpsSettingsResponse getGpsSettings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return bossStoreFacade.getGpsSettings(storeId, userId);
    }

    @PostMapping("/{storeId}/attendance-settings/gps")
    public GpsSettingsResponse updateGps(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @PathVariable Long storeId,
                                         @RequestBody @Valid GpsRegisterRequest request) {
        final Long userId = userDetails.getUserId();
        return bossStoreFacade.updateGpsSettings(storeId, userId, request);
    }
}