package com.mangoboss.app.api.controller.staff;

import com.mangoboss.app.api.facade.staff.StaffStaffFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.payroll.response.AccountRegisterResponse;
import com.mangoboss.app.dto.staff.request.StaffInfoResponse;
import com.mangoboss.app.dto.staff.request.StaffAccountRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/staff/stores/{storeId}/staffs")
@PreAuthorize("hasRole('STAFF')")
public class StaffStaffController {
    private final StaffStaffFacade staffStaffFacade;

    @PostMapping("/account-verification")
    public AccountRegisterResponse verifyAndRegisterAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Long storeId, @RequestBody @Valid StaffAccountRegisterRequest request) {
        final Long userId = userDetails.getUserId();
        return staffStaffFacade.registerStaffAccount(storeId, userId, request);
    }

    @GetMapping ("/account")
    public AccountRegisterResponse getAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return staffStaffFacade.getStaffAccount(storeId, userId);
    }

    @GetMapping ("/my")
    public StaffInfoResponse getStaffInfo(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return staffStaffFacade.getStaffInfo(storeId, userId);
    }

    @DeleteMapping ("/account")
    public void deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        staffStaffFacade.deleteAccount(storeId, userId);
    }
}
