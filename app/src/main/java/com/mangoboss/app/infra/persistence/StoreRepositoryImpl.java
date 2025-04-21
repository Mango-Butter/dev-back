package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
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
        return storeJpaRepository.existsByQrCode(qrCode);
    }

    @Override
    public boolean existsByIdAndBossId(final Long storeId, final Long userId) {
        return storeJpaRepository.existsByIdAndBossId(storeId, userId);
    }

    @Override
    public StoreEntity save(final StoreEntity storeEntity) {
        return storeJpaRepository.save(storeEntity);
    }

    @Override
    public StoreEntity getByInviteCode(final String inviteCode) {
        return storeJpaRepository.findByInviteCode(inviteCode)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.INVITE_CODE_NOT_FOUND));
    }

    @Override
    public StoreEntity getById(final Long id) {
        return storeJpaRepository.findById(id)
                .orElseThrow(()->new CustomException(CustomErrorInfo.STORE_NOT_FOUND));
    }

}
