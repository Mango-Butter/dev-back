package com.mangoboss.app.domain.service.staff;

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
        final StaffEntity staff = StaffEntity.create(user, store);
        return staffRepository.save(staff);
    }
}
