package com.mangoboss.app.api.controller.store.boss;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mangoboss.app.api.facade.store.StoreFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.store.BusinessNumberRequest;
import com.mangoboss.app.dto.store.StoreCreateRequest;
import com.mangoboss.app.dto.store.StoreCreateResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boss/stores")
@PreAuthorize("hasRole('BOSS')")
public class BossStoreController {
	private final StoreFacade storeFacade;

	@PostMapping
	public StoreCreateResponse createStore(@RequestBody StoreCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		final Long userId = userDetails.getUserId();
		return storeFacade.createStore(userId, request);
	}

	@PreAuthorize("hasRole('BOSS')")
	@PostMapping("/validations/business-number")
	public void validateBusinessNumber(@RequestBody BusinessNumberRequest request) {
		storeFacade.validateBusinessNumber(request.businessNumber());
	}
}