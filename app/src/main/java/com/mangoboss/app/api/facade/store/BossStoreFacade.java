package com.mangoboss.app.api.facade.store;

import com.mangoboss.app.domain.service.payroll.PayrollSettingService;
import com.mangoboss.app.dto.store.response.*;
import com.mangoboss.app.dto.store.request.StoreUpdateRequest;

import com.mangoboss.app.dto.store.request.AttendanceSettingsRequest;
import org.springframework.stereotype.Service;

import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.store.request.StoreCreateRequest;
import com.mangoboss.app.dto.store.request.GpsRegisterRequest;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BossStoreFacade {
	private final StoreService storeService;
	private final UserService userService;
	private final PayrollSettingService payrollSettingService;

	@Transactional
	public StoreCreateResponse createStore(final Long userId, final StoreCreateRequest request) {
		final UserEntity boss = userService.getUserById(userId);
		storeService.validateBusinessNumber(request.businessNumber());
		final StoreEntity saved = storeService.createStore(request, boss);
		payrollSettingService.initPayrollSettingForStore(saved);
		return StoreCreateResponse.fromEntity(saved);
	}

	public void validateBusinessNumber(final String businessNumber) {
		storeService.validateBusinessNumber(businessNumber);
	}

	public List<BossStoreInfoResponse> getMyStores(final Long userId) {
		final List<StoreEntity> stores = storeService.getStoresByBossId(userId);
		return stores.stream()
				.map(BossStoreInfoResponse::fromEntity)
				.toList();
	}

	public BossStoreInfoResponse getStoreInfo(final Long storeId, final Long userId) {
		final StoreEntity store = storeService.isBossOfStore(storeId, userId);
		return BossStoreInfoResponse.fromEntity(store);
	}

	public void updateStoreInfo(final Long storeId, final Long userId, final StoreUpdateRequest request) {
		storeService.isBossOfStore(storeId, userId);
		storeService.updateStoreInfo(storeId, request.address(), request.storeType());
	}

	public StoreInviteCodeResponse reissueInviteCode(final Long storeId, final Long userId) {
		storeService.isBossOfStore(storeId, userId);
		final String newInviteCode = storeService.reissueInviteCode(storeId);
		return StoreInviteCodeResponse.of(newInviteCode);
	}

	public AttendanceSettingsResponse getAttendanceSettings(final Long storeId, final Long userId) {
		storeService.isBossOfStore(storeId, userId);
		final StoreEntity store = storeService.getStoreById(storeId);
		return AttendanceSettingsResponse.of(store.getAttendanceMethod());
	}

	public QrCodeResponse getQrSettings(final Long storeId, final Long userId) {
		storeService.isBossOfStore(storeId, userId);
		final StoreEntity store = storeService.getStoreById(storeId);
		return QrCodeResponse.of(store.getId(), store.getQrCode());
	}

	public GpsSettingsResponse getGpsSettings(final Long storeId, final Long userId) {
		storeService.isBossOfStore(storeId, userId);
		final StoreEntity store = storeService.getStoreById(storeId);
		return GpsSettingsResponse.fromEntity(store);
	}

	public AttendanceSettingsResponse updateAttendanceSettings(final Long storeId, final Long userId, final AttendanceSettingsRequest request) {
		storeService.isBossOfStore(storeId, userId);
		final StoreEntity store = storeService.updateAttendanceSettings(storeId, request.attendanceMethod());
		return AttendanceSettingsResponse.of(store.getAttendanceMethod());
	}

	public QrCodeResponse regenerateQrCode(final Long storeId, final Long userId) {
		storeService.isBossOfStore(storeId, userId);
		final String newQr = storeService.regenerateQrCode(storeId);
		return QrCodeResponse.of(storeId, newQr);
	}

	public GpsSettingsResponse updateGpsSettings(final Long storeId, final Long userId, final GpsRegisterRequest request) {
		storeService.isBossOfStore(storeId, userId);
		final StoreEntity store = storeService.updateGpsSettings(storeId, request.address(), request.gpsLatitude(), request.gpsLongitude(), request.gpsRangeMeters());
		return GpsSettingsResponse.fromEntity(store);
	}
}
