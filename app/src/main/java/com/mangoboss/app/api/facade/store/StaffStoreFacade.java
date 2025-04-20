package com.mangoboss.app.api.facade.store;

import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.store.StaffJoinRequest;
import com.mangoboss.app.dto.store.StaffJoinResponse;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffStoreFacade {
    private final StoreService storeService;
    private final UserService userService;
    private final StaffService staffService;

    public StaffJoinResponse joinStaff(final Long userId, final StaffJoinRequest request) {
        final UserEntity user = userService.getUserById(userId);
        final StoreEntity store = storeService.getStoreByInviteCode(request.inviteCode());
        staffService.createStaff(user, store);
        return StaffJoinResponse.fromEntity(store);
    }
}
