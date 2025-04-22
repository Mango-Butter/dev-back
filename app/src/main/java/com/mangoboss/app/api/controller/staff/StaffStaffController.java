package com.mangoboss.app.api.controller.staff;


import com.mangoboss.app.api.facade.staff.StaffStaffFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.staff.request.RegularGroupCreateRequest;
import com.mangoboss.app.dto.staff.response.RegularGroupResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/boss/stores/{storeId}/staffs/{staffId}")
@PreAuthorize("hasRole('BOSS')")
public class StaffStaffController {
    private final StaffStaffFacade staffStaffFacade;

    @PostMapping("/schedules/regular")
    public void createRegularSchedule(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @PathVariable Long storeId, @PathVariable Long staffId,
                                      @RequestBody @Valid List<RegularGroupCreateRequest> requestList) {
        final Long userId = userDetails.getUserId();
        staffStaffFacade.createRegularSchedules(storeId, staffId, userId, requestList);
    }

    @GetMapping("schedules/regular")
    public ListWrapperResponse<RegularGroupResponse> getRegularGroups(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @PathVariable Long storeId, @PathVariable Long staffId){
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(
                staffStaffFacade.getRegularGroupsForStaff(storeId,staffId,userId));
    }
}
