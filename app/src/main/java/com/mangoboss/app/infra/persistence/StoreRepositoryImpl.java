package com.mangoboss.app.infra.persistence;

import java.util.Optional;

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
	public StoreEntity save(final StoreEntity storeEntity) {
		return storeJpaRepository.save(storeEntity);
	}

	@Override
	public Optional<StoreEntity> findById(final Long id) {
		return storeJpaRepository.findById(id);
	}
}