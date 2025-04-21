package com.mangoboss.app.domain.service.staff;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StaffRepository;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public StaffEntity getStaffBelongsToStore(final Long storeId, final Long staffId){
        return staffRepository.getByIdAndStoreId(staffId, storeId);
    }

    private void isAlreadyJoin(final UserEntity user, final StoreEntity store) {
        if (staffRepository.existsByUserIdAndStoreId(user.getId(), store.getId())) {
            throw new CustomException(CustomErrorInfo.ALREADY_JOIN_STAFF);
        }
    }
}
