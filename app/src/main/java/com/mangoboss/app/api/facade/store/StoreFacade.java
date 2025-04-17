package com.mangoboss.app.api.facade.store;

import org.springframework.stereotype.Service;

import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.store.StoreCreateRequest;
import com.mangoboss.app.dto.store.StoreCreateResponse;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreFacade {
	private final StoreService storeService;
	private final UserService userService;

	public StoreCreateResponse createStore(final Long userId, final StoreCreateRequest request) {
		final UserEntity boss = userService.getByUserId(userId);
		validateBusinessNumber(request.businessNumber());
		StoreEntity saved = storeService.createStore(request, boss);
		return StoreCreateResponse.builder()
			.storeId(saved.getStoreId())
			.build();
	}

	public void validateBusinessNumber(final String businessNumber) {
		storeService.validateBusinessNumber(businessNumber);
	}
}
