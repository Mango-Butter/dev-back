package com.mangoboss.app.domain.service.store;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.ExternalBusinessApiClient;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StoreRepository;
import com.mangoboss.app.dto.store.StoreCreateRequest;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
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

	@Transactional
	public StoreEntity createStore(final StoreCreateRequest request, final UserEntity boss) {
		validateBusinessNumber(request.businessNumber());
		String inviteCode = generateInviteCode();
		String attendanceQrCode = generateQrCode();
		StoreEntity store = StoreEntity.create(
			boss,
			request.storeName(),
			request.address(),
			request.storeType(),
			request.businessNumber(),
			inviteCode,
			request.gps().latitude(),
			request.gps().longitude(),
			attendanceQrCode
		);
		return storeRepository.save(store);
	}

	private String generateInviteCode() {
		// 영문+숫자 6자리
		return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
	}

	private String generateQrCode() {
		return RandomStringUtils.randomAlphanumeric(12).toUpperCase();
	}
}