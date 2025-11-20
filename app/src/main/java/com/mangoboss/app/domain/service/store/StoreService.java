package com.mangoboss.app.domain.service.store;

import java.security.SecureRandom;
import java.util.List;

import com.mangoboss.storage.store.StoreType;
import com.mangoboss.storage.store.AttendanceMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.external.national_tax_service.ExternalBusinessApiClient;
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
    private static final int PLAN_LIMIT_STORE_NUM = 2;
    private static final int INVITE_CODE_LENGTH = 6;
    private static final int QR_CODE_LENGTH = 12;

    private final SecureRandom random = new SecureRandom();
    private final StoreRepository storeRepository;
    private final ExternalBusinessApiClient externalBusinessApiClient;

    public StoreEntity isBossOfStore(final Long storeId, final Long userId) {
        return storeRepository.getByIdAndBossId(storeId, userId);
    }

    public boolean isDuplicatedBusinessNumber(final String businessNumber) {
        return storeRepository.existsByBusinessNumber(businessNumber);
    }

    public void validateBusinessNumber(final String businessNumber) {
        if (!externalBusinessApiClient.checkBusinessNumberValid(businessNumber)) {
            throw new CustomException(CustomErrorInfo.INVALID_BUSINESS_NUMBER);
        }

        if (isDuplicatedBusinessNumber(businessNumber)) {
            throw new CustomException(CustomErrorInfo.DUPLICATE_BUSINESS_NUMBER);
        }
    }

    public StoreEntity getStoreByInviteCode(final String inviteCode) {
        return storeRepository.getByInviteCode(inviteCode);
    }

    public StoreEntity getStoreById(final Long storeId) {
        return storeRepository.getById(storeId);
    }

    @Transactional
    public StoreEntity createStore(final StoreCreateRequest request, final UserEntity boss) {
        if (boss.getSubscription() == null && getStoresNum(boss.getId()) >= PLAN_LIMIT_STORE_NUM){
            throw new CustomException(CustomErrorInfo.PLAN_LIMIT_EXCEEDED);
        }
        // 국세청_사업자등록정보 진위확인 및 상태조회 서비스(공공데이터포털 오픈API) 오류에 따른 임시 주석처리
        // validateBusinessNumber(request.businessNumber()); 
        final String inviteCode = generateInviteCode();
        final String attendanceQrCode = generateQrCode();
        final StoreEntity store = request.toEntity(boss, inviteCode, attendanceQrCode);
        return storeRepository.save(store);
    }

    private Integer getStoresNum(final Long bossId) {
        return storeRepository.findAllByBossId(bossId).size();
    }

    private String generateInviteCode() {
        String code;
        do {
            code = randomString(INVITE_CODE_LENGTH);
        } while (storeRepository.existsByInviteCode(code));
        return code;
    }

    private String generateQrCode() {
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

    public List<StoreEntity> getStoresByBossId(final Long bossId) {
        return storeRepository.findAllByBossId(bossId);
    }

    @Transactional
    public void updateStoreInfo(final Long storeId, final String address, final StoreType storeType,
                                final Double gpsLatitude, final Double gpsLongitude, final Integer overtimeLimit) {
        final StoreEntity store = getStoreById(storeId);
        store.updateInfo(address, storeType, gpsLatitude, gpsLongitude, overtimeLimit);
    }

    @Transactional
    public String reissueInviteCode(final Long storeId) {
        final StoreEntity store = storeRepository.getById(storeId);
        final String newInviteCode = generateInviteCode();
        store.updateInviteCode(newInviteCode);
        return newInviteCode;
    }

    @Transactional
    public String regenerateQrCode(final Long storeId) {
        final StoreEntity store = getStoreById(storeId);
        final String newQrCode = generateQrCode();
        store.updateQrCode(newQrCode);
        return newQrCode;
    }

    @Transactional
    public StoreEntity updateGpsSettings(final Long storeId, final String address, final Double latitude, final Double longitude, final Integer gpsRangeMeters) {
        final StoreEntity store = getStoreById(storeId);
        return store.updateGpsSettings(address, latitude, longitude, gpsRangeMeters);
    }

    @Transactional
    public StoreEntity updateAttendanceSettings(final Long storeId, final AttendanceMethod method) {
        final StoreEntity store = getStoreById(storeId);
        return store.updateAttendanceMethod(method);
    }
}
