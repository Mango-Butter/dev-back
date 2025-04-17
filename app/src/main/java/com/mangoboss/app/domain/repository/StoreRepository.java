package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.store.StoreEntity;
import java.util.Optional;

public interface StoreRepository {
	boolean existsByBusinessNumber(final String businessNumber);
	StoreEntity save(final StoreEntity storeEntity);
	Optional<StoreEntity> findById(final Long id);
}
