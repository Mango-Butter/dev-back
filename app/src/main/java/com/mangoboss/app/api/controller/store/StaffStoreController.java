package com.mangoboss.app.api.controller.store;

import com.mangoboss.app.api.facade.store.StaffStoreFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.store.request.StaffJoinRequest;
import com.mangoboss.app.dto.store.response.StaffJoinResponse;
import com.mangoboss.app.dto.store.response.StaffStoreInfoResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/stores")
@PreAuthorize("hasRole('STAFF')")
public class StaffStoreController {

    private final StaffStoreFacade staffStoreFacade;

    @PostMapping
    public StaffJoinResponse joinStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody @Valid StaffJoinRequest request) {
        final Long userId = userDetails.getUserId();
        return staffStoreFacade.joinStaff(userId, request);
    }

    @GetMapping
    public ListWrapperResponse<StaffStoreInfoResponse> getMyStores(@AuthenticationPrincipal CustomUserDetails userDetails) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(staffStoreFacade.getMyStores(userId));
    }
}
