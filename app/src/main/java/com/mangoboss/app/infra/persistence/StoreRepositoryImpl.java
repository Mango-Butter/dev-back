package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import org.springframework.stereotype.Repository;

import com.mangoboss.app.domain.repository.StoreRepository;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.store.StoreJpaRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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
                .orElseThrow(() -> new CustomException(CustomErrorInfo.STORE_NOT_FOUND));
    }

    @Override
    public List<StoreEntity> findAllByBossId(final Long bossId) {
        return storeJpaRepository.findAllByBossId(bossId);
    }

    @Override
    public StoreEntity getByIdAndBossId(final Long id, final Long bossId) {
        return storeJpaRepository.findByIdAndBossId(id, bossId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.NOT_STORE_BOSS));
    }

    @Override
    public List<StoreEntity> findAllByUserId(final Long userId) {
        return storeJpaRepository.findAllByUserId(userId);
    }

}
