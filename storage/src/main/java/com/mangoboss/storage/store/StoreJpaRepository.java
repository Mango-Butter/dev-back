package com.mangoboss.storage.store;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreJpaRepository extends JpaRepository<StoreEntity, Long> {
	boolean existsByBusinessNumber(String businessNumber);
	boolean existsByInviteCode(String inviteCode);
	boolean existsByAttendanceQrCode(String qrCode);
}