package com.mangoboss.app.domain.service.store;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.ExternalBusinessApiClient;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StoreRepository;
import com.mangoboss.app.dto.store.request.StoreCreateRequest;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {
	private final StoreRepository storeRepository;
	private final ExternalBusinessApiClient externalBusinessApiClient;

	@Transactional(readOnly = true)
	public boolean isDuplicatedBusinessNumber(final String businessNumber) {
		return storeRepository.existsByBusinessNumber(businessNumber);
	}

	@Transactional(readOnly = true)
	public void validateBusinessNumber(final String businessNumber) {
		if (!externalBusinessApiClient.checkBusinessNumberValid(businessNumber)) {
			throw new CustomException(CustomErrorInfo.INVALID_BUSINESS_NUMBER);
		}

		if (isDuplicatedBusinessNumber(businessNumber)) {
			throw new CustomException(CustomErrorInfo.DUPLICATE_BUSINESS_NUMBER);
		}
	}

	public StoreEntity createStore(final StoreCreateRequest request, final UserEntity boss) {
		validateBusinessNumber(request.businessNumber());
		final String inviteCode = generateInviteCode();
		final String attendanceQrCode = generateQrCode();
		final StoreEntity store = request.toEntity(boss, inviteCode, attendanceQrCode);
		return storeRepository.save(store);
	}

	private String generateInviteCode() {
		// 중복 방지를 위해 초대 코드를 생성하고 DB에 존재하는지 검사
		String code;
		do {
			code = RandomStringUtils.randomAlphanumeric(6).toUpperCase(); // 영문+숫자 6자리
		} while (storeRepository.existsByInviteCode(code));
		return code;
	}

	private String generateQrCode() {
		// 중복 방지를 위해 QR 코드를 생성하고 DB에 존재하는지 검사
		String code;
		do {
			code = RandomStringUtils.randomAlphanumeric(12).toUpperCase();  // 영문+숫자 12자리
		} while (storeRepository.existsByAttendanceQrCode(code));
		return code;
	}
}