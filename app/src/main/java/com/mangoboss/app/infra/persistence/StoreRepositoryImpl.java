package com.mangoboss.app.infra.persistence;

import org.springframework.stereotype.Repository;

import com.mangoboss.app.domain.repository.StoreRepository;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.store.StoreJpaRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

	private final StoreJpaRepository storeJpaRepository;

	@Override
	public boolean existsByBusinessNumber(final String businessNumber) {
		return storeJpaRepository.existsByBusinessNumber(businessNumber);
	}

	@Override
	public boolean existsByInviteCode(final String inviteCode) {
		return storeJpaRepository.existsByInviteCode(inviteCode);
	}

	@Override
	public boolean existsByAttendanceQrCode(final String qrCode) {
		return storeJpaRepository.existsByAttendanceQrCode(qrCode);
	}

	@Override
	public StoreEntity save(final StoreEntity storeEntity) {
		return storeJpaRepository.save(storeEntity);
	}
}