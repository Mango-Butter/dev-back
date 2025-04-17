package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.store.StoreEntity;
import java.util.Optional;

public interface StoreRepository {
	boolean existsByBusinessNumber(String businessNumber);
	boolean existsByInviteCode(String inviteCode);
	boolean existsByAttendanceQrCode(String qrCode);
	StoreEntity save(StoreEntity storeEntity);
}
