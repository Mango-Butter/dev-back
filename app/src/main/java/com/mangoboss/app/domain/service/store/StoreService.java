package com.mangoboss.app.domain.service.store;

import java.security.SecureRandom;
import java.util.List;

import com.mangoboss.storage.store.StoreType;
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
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int INVITE_CODE_LENGTH = 6;
    private static final int QR_CODE_LENGTH = 12;

    private final SecureRandom random = new SecureRandom();
    private final StoreRepository storeRepository;
    private final ExternalBusinessApiClient externalBusinessApiClient;

    @Transactional(readOnly = true)
    public void isBossOfStore(final Long storeId, final Long userId){
        if(!storeRepository.existsByIdAndBossId(storeId,userId)){
            throw new CustomException(CustomErrorInfo.NOT_STORE_BOSS);
        }
    }

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

    @Transactional(readOnly = true)
    public StoreEntity getStoreByInviteCode(final String inviteCode) {
        return storeRepository.getByInviteCode(inviteCode);
    }

    @Transactional(readOnly = true)
    public StoreEntity getStoreById(final Long storeId){
        return storeRepository.getById(storeId);
    }

    public StoreEntity createStore(final StoreCreateRequest request, final UserEntity boss) {
        validateBusinessNumber(request.businessNumber());
        final String inviteCode = generateInviteCode();
        final String attendanceQrCode = generateQrCode();
        final StoreEntity store = request.toEntity(boss, inviteCode, attendanceQrCode);
        return storeRepository.save(store);
    }

    private String generateInviteCode() {
        String code;
        do {
            code = randomString(INVITE_CODE_LENGTH);
        } while (storeRepository.existsByInviteCode(code));
        return code;
    }

    private String generateQrCode() {
        // 중복 방지를 위해 QR 코드를 생성하고 DB에 존재하는지 검사
        String code;
        do {
            code = randomString(QR_CODE_LENGTH);
        } while (storeRepository.existsByAttendanceQrCode(code));
        return code;
    }

    private String randomString(final int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return sb.toString();
    }

    @Transactional(readOnly = true)
    public List<StoreEntity> getStoresByBossId(final Long bossId) {
        return storeRepository.findAllByBossId(bossId);
    }

    public void updateStoreInfo(final Long storeId, final String address, final StoreType storeType) {
        final StoreEntity store = getStoreById(storeId);
        store.updateInfo(address, storeType);
    }

    public String reissueInviteCode(final Long storeId) {
        final StoreEntity store = storeRepository.getById(storeId);
        final String newInviteCode = generateInviteCode();
        store.updateInviteCode(newInviteCode);
        return newInviteCode;
    }
}
