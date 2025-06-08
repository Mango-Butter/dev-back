package com.mangoboss.app.api.controller.staff;


import com.mangoboss.app.api.facade.staff.BossStaffFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.staff.request.StaffHourlyWageRequest;
import com.mangoboss.app.dto.staff.request.StaffWithholdingRequest;
import com.mangoboss.app.dto.staff.request.RegularGroupCreateRequest;
import com.mangoboss.app.dto.staff.response.RegularGroupResponse;
import com.mangoboss.app.dto.staff.response.StaffHourlyWageResponse;
import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.app.dto.staff.response.StaffWithholdingTypeResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/boss/stores/{storeId}/staffs")
@PreAuthorize("hasRole('BOSS')")
public class BossStaffController {
    private final BossStaffFacade bossStaffFacade;

    @PostMapping("/{staffId}/regular")
    public void createRegularSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @PathVariable Long storeId, @PathVariable Long staffId,
                                      @RequestBody @Valid List<RegularGroupCreateRequest> requestList) {
        final Long userId = userDetails.getUserId();
        bossStaffFacade.createRegularSchedules(storeId, staffId, userId, requestList);
    }

    @GetMapping("/{staffId}/regular")
    public ListWrapperResponse<RegularGroupResponse> getRegularGroups(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable Long storeId, @PathVariable Long staffId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(
                bossStaffFacade.getRegularGroupsForStaff(storeId, staffId, userId));
    }

    @DeleteMapping("/{staffId}/regular/{regularId}")
    public void terminateRegularGroup(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @PathVariable Long storeId, @PathVariable Long staffId, @PathVariable Long regularId) {
        final Long userId = userDetails.getUserId();
        bossStaffFacade.terminateRegularGroup(storeId, userId, regularId);
    }

    @GetMapping("/brief")
    public ListWrapperResponse<StaffSimpleResponse> getStaffsForStore(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                      @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossStaffFacade.getStaffsForStore(storeId, userId));
    }

    @GetMapping("/{staffId}")
    public StaffSimpleResponse getStaffDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @PathVariable Long storeId,
                                              @PathVariable Long staffId) {
        final Long userId = userDetails.getUserId();
        return bossStaffFacade.getStaffDetail(storeId, staffId, userId);
    }

    @GetMapping("/withholding")
    public ListWrapperResponse<StaffWithholdingTypeResponse> updateWithholdingType(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                   @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossStaffFacade.getStaffWithholdingTypes(storeId, userId));
    }

    @PutMapping("/{staffId}/withholding")
    public void updateStaffWithholdingType(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable Long storeId,
                                           @PathVariable Long staffId,
                                           @RequestBody @Valid StaffWithholdingRequest request) {
        final Long userId = userDetails.getUserId();
        bossStaffFacade.updateStaffWithholdingType(storeId, staffId, userId, request);
    }

    @GetMapping("/hourly-wage")
    public ListWrapperResponse<StaffHourlyWageResponse> updateHourlyWage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossStaffFacade.getStaffWithHourlyWage(storeId, userId));
    }

    @PutMapping("/{staffId}/hourly-wage")
    public void updateStaffHourlyWage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @PathVariable Long storeId,
                                      @PathVariable Long staffId,
                                      @RequestBody @Valid StaffHourlyWageRequest request) {
        final Long userId = userDetails.getUserId();
        bossStaffFacade.updateStaffHourlyWage(storeId, staffId, userId, request);
    }
}
