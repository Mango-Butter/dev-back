package com.mangoboss.app.api.facade.store;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.service.attendance.AttendanceEditService;
import com.mangoboss.app.domain.service.document.RequiredDocumentService;
import com.mangoboss.app.domain.service.payroll.PayrollSettingService;
import com.mangoboss.app.domain.service.schedule.SubstituteRequestService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.store.response.*;
import com.mangoboss.app.dto.store.request.StoreUpdateRequest;

import com.mangoboss.app.dto.store.request.AttendanceSettingsRequest;
import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.schedule.SubstituteRequestEntity;
import com.mangoboss.storage.staff.StaffEntity;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BossStoreFacade {
	private final StoreService storeService;
    private final StaffService staffService;
	private final UserService userService;
	private final PayrollSettingService payrollSettingService;
	private final RequiredDocumentService requiredDocumentService;

	private final SubstituteRequestService substituteRequestService;
	private final AttendanceEditService attendanceEditService;

	@Transactional
	public StoreCreateResponse createStore(final Long userId, final StoreCreateRequest request) {
		final UserEntity boss = userService.getUserById(userId);
		final StoreEntity saved = storeService.createStore(request, boss);
		payrollSettingService.initPayrollSettingForStore(saved);
		requiredDocumentService.initRequiredDocuments(saved);
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
		storeService.updateStoreInfo(storeId, request.address(), request.storeType(), request.gps().latitude(), request.gps().longitude(), request.overtimeLimit());
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

	public StoreRequestedResponse getRequestsForStore(final Long storeId, final Long userId) {
		storeService.isBossOfStore(storeId, userId);
		List<SubstituteRequestEntity> substituteRequests = substituteRequestService.getRecentIncompleteRequestsByStoreId(storeId);
		List<AttendanceEditEntity> attendanceEdits = attendanceEditService.getRecentIncompleteEditsByStoreId(storeId);
		Integer requestedCount = substituteRequests.size() + attendanceEdits.size();
		List<Object> mergedList = mergeAndSortByCreatedAt(substituteRequests, attendanceEdits);
        List<StaffEntity> staffs = mergedList.stream()
                .map(this::extractStaffId)
                .map(staffService::getStaffById)
                .toList();
        return StoreRequestedResponse.of(requestedCount, staffs);
	}

	private List<Object> mergeAndSortByCreatedAt(List<?> list1, List<?> list2) {
        int limit = 4;
		List<Object> combined = new ArrayList<>();
		combined.addAll(list1);
		combined.addAll(list2);

		return combined.stream()
				.sorted(Comparator.comparing(this::extractCreatedAt).reversed())
				.limit(limit)
				.toList();
	}

	private LocalDateTime extractCreatedAt(Object entity) {
		if (entity instanceof SubstituteRequestEntity sub) {
			return sub.getCreatedAt();
		} else if (entity instanceof AttendanceEditEntity edit) {
			return edit.getCreatedAt();
		}
		throw new CustomException(CustomErrorInfo.INVALID_TYPE);
	}

	private Long extractStaffId(Object entity) {
		if (entity instanceof SubstituteRequestEntity sub) {
			return sub.getRequesterStaffId();
		} else if (entity instanceof AttendanceEditEntity edit) {
			return edit.getStaffId();
		}
		throw new CustomException(CustomErrorInfo.INVALID_TYPE);
	}
}
