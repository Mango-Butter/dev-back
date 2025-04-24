package com.mangoboss.app.api.controller.store;

import com.mangoboss.app.api.facade.store.StaffStoreFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.store.request.StaffJoinRequest;
import com.mangoboss.app.dto.store.response.StaffJoinResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/staff/stores")
@PreAuthorize("hasRole('STAFF')")
public class StaffStoreController {

    private final StaffStoreFacade staffStoreFacade;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public StaffJoinResponse joinStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @RequestBody @Valid StaffJoinRequest request) {
        final Long userId = userDetails.getUserId();
        return staffStoreFacade.joinStaff(userId, request);
    }
}
