package com.mangoboss.storage.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreJpaRepository extends JpaRepository<StoreEntity, Long> {
	boolean existsByBusinessNumber(String businessNumber);
}