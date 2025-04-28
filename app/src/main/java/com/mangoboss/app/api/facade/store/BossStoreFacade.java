package com.mangoboss.app.api.facade.store;

import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.store.response.*;
import com.mangoboss.app.dto.store.request.StoreUpdateRequest;

import com.mangoboss.app.dto.store.request.AttendanceMethodUpdateRequest;
import org.springframework.stereotype.Service;

import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.store.request.StoreCreateRequest;
import com.mangoboss.app.dto.store.request.GpsRegisterRequest;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BossStoreFacade {
	private final StoreService storeService;
	private final UserService userService;

	public StoreCreateResponse createStore(final Long userId, final StoreCreateRequest request) {
		final UserEntity boss = userService.getUserById(userId);
		storeService.validateBusinessNumber(request.businessNumber());
		final StoreEntity saved = storeService.createStore(request, boss);
		return StoreCreateResponse.fromEntity(saved);
	}

	public void validateBusinessNumber(final String businessNumber) {
		storeService.validateBusinessNumber(businessNumber);
	}

	public List<StoreListResponse> getMyStores(final Long userId) {
		final List<StoreEntity> stores = storeService.getStoresByBossId(userId);
		return stores.stream()
				.map(StoreListResponse::fromEntity)
				.toList();
	}

	public StoreInfoResponse getStoreInfo(final Long userId, final Long storeId) {
		storeService.isBossOfStore(userId, storeId);
		final StoreEntity store = storeService.getStoreById(storeId);
		return StoreInfoResponse.fromEntity(store);
	}

	public void updateStoreInfo(final Long userId, final Long storeId, final StoreUpdateRequest request) {
		storeService.isBossOfStore(userId, storeId);
		storeService.updateStoreInfo(storeId, request.address(), request.storeType());
	}

	public StoreInviteCodeResponse reissueInviteCode(final Long userId, final Long storeId) {
		storeService.isBossOfStore(userId, storeId);
		final String newInviteCode = storeService.reissueInviteCode(storeId);
		return StoreInviteCodeResponse.of(newInviteCode);
	}

	public AttendanceSettingsResponse getAttendanceSettings(final Long userId, final Long storeId) {
		storeService.isBossOfStore(userId, storeId);
		final StoreEntity store = storeService.getStoreById(storeId);
		return AttendanceSettingsResponse.fromEntity(store);
	}

	public QrCodeResponse getQrSettings(final Long userId, final Long storeId) {
		storeService.isBossOfStore(userId, storeId);
		final StoreEntity store = storeService.getStoreById(storeId);
		return QrCodeResponse.of(store.getId(), store.getQrCode());
	}

	public GpsSettingsResponse getGpsSettings(final Long userId, final Long storeId) {
		storeService.isBossOfStore(userId, storeId);
		final StoreEntity store = storeService.getStoreById(storeId);
		return GpsSettingsResponse.fromEntity(store);
	}

	public AttendanceSettingsResponse updateAttendanceSettings(final Long userId, final Long storeId, final AttendanceMethodUpdateRequest request) {
		storeService.isBossOfStore(userId, storeId);
		storeService.updateAttendanceSettings(storeId, request.useQr(), request.useGps());
		final StoreEntity store = storeService.getStoreById(storeId);
		return AttendanceSettingsResponse.fromEntity(store);
	}

	public QrCodeResponse regenerateQrCode(final Long userId, final Long storeId) {
		storeService.isBossOfStore(userId, storeId);
		final String newQr = storeService.regenerateQrCode(storeId);
		return QrCodeResponse.of(storeId, newQr);
	}

	public GpsSettingsResponse updateGpsSettings(final Long userId, final Long storeId, final GpsRegisterRequest request) {
		storeService.isBossOfStore(userId, storeId);
		storeService.updateGpsSettings(storeId, request.address(), request.latitude(), request.longitude(), request.gpsRangeMeters());
		final StoreEntity store = storeService.getStoreById(storeId);
		return GpsSettingsResponse.fromEntity(store);
	}
}
