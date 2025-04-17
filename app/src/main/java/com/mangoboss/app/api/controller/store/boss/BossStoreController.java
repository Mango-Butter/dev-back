package com.mangoboss.app.api.controller.store.boss;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mangoboss.app.api.facade.store.boss.BossStoreFacade;
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
}