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
@Transactional
@RequiredArgsConstructor
public class StaffService {
    private final StaffRepository staffRepository;

    public StaffEntity createStaff(final UserEntity user, final StoreEntity store) {
        isAlreadyJoin(user, store);
        final StaffEntity staff = StaffEntity.create(user, store);
        return staffRepository.save(staff);
    }

    @Transactional(readOnly = true)
    public StaffEntity validateStaffBelongsToStore(final Long storeId, final Long staffId) {
        return staffRepository.getByIdAndStoreId(staffId, storeId);
    }

    private void isAlreadyJoin(final UserEntity user, final StoreEntity store) {
        if (staffRepository.existsByUserIdAndStoreId(user.getId(), store.getId())) {
            throw new CustomException(CustomErrorInfo.ALREADY_JOIN_STAFF);
        }
    }

    @Transactional(readOnly = true)
    public List<StaffEntity> getStaffsForStore(final Long storeId) {
        return staffRepository.findAllByStoreId(storeId);
    }

    @Transactional(readOnly = true)
    public StaffEntity getVerifiedStaff(final Long userId, final Long storeId) {
        return staffRepository.getByUserIdAndStoreId(userId, storeId);
    }

    @Transactional(readOnly = true)
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
}
