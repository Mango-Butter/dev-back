package com.mangoboss.app.domain.service.staff;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StaffRepository;
import com.mangoboss.app.dto.payroll.response.AccountRegisterResponse;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.payroll.WithholdingType;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StaffService {
    private static final int PLAN_LIMIT_STAFF_NUM = 5;
    private final StaffRepository staffRepository;

    @Transactional
    public StaffEntity createStaff(final UserEntity user, final StoreEntity store, final UserEntity boss) {
        isAlreadyJoin(user, store);
        if (boss.getSubscription() == null && getStaffsNum(store.getId()) >= PLAN_LIMIT_STAFF_NUM) {
            throw new CustomException(CustomErrorInfo.PLAN_LIMIT_EXCEEDED);
        }
        final StaffEntity staff = StaffEntity.create(user, store);
        return staffRepository.save(staff);
    }

    private Integer getStaffsNum(final Long storeId) {
        return getStaffsForStore(storeId).size();
    }

    public StaffEntity validateStaffBelongsToStore(final Long storeId, final Long staffId) {
        return staffRepository.getByIdAndStoreId(staffId, storeId);
    }

    private void isAlreadyJoin(final UserEntity user, final StoreEntity store) {
        if (staffRepository.existsByUserIdAndStoreId(user.getId(), store.getId())) {
            throw new CustomException(CustomErrorInfo.ALREADY_JOIN_STAFF);
        }
    }

    public List<StaffEntity> getStaffsForStore(final Long storeId) {
        return staffRepository.findAllByStoreId(storeId);
    }

    public StaffEntity getVerifiedStaff(final Long userId, final Long storeId) {
        return staffRepository.getByUserIdAndStoreId(userId, storeId);
    }

    public StaffEntity getStaffById(final Long staffId) {
        return staffRepository.getById(staffId);
    }

    @Transactional
    public void updateHourlyWage(final StaffEntity staff, final Integer hourlyWage) {
        staff.updateHourlyWage(hourlyWage);
    }

    @Transactional
    public void updateWithholdingType(final StaffEntity staff, final WithholdingType withholdingType) {
        staff.updateWithholdingType(withholdingType);
    }

    @Transactional
    public void registerStaffAccount(final StaffEntity staff, final BankCode bankCode, final String account) {
        staff.registerAccount(bankCode, account);
    }

    @Transactional
    public void deleteAccount(final StaffEntity staff) {
        staff.deleteAccount();
    }

    public List<StaffEntity> getStaffsByUserId(final Long userId) {
        return staffRepository.findAllByUserId(userId);
    }
}
