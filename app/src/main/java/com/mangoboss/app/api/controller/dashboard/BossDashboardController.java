package com.mangoboss.app.api.controller.dashboard;

import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.api.facade.dashboard.BossDashboardFacade;
import com.mangoboss.app.dto.dashboard.response.StaffAttendanceSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boss/stores/{storeId}/staffs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossDashboardController {

    private final BossDashboardFacade bossDashboardFacade;

    @GetMapping("/attendances")
    public ListWrapperResponse<StaffAttendanceSummaryResponse> getStaffAttendanceDashboard(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                           @PathVariable Long storeId) {
        final Long bossId = userDetails.getUserId();
        return ListWrapperResponse.of(bossDashboardFacade.getStaffAttendanceDashboard(storeId, bossId));
    }
}