package com.mangoboss.app.api.controller.attendance;

import com.mangoboss.app.api.facade.attendance.BossAttendanceFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.attendance.response.AttendanceDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boss/stores/{storeId}/schedules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossAttendanceController {
    private final BossAttendanceFacade bossAttendanceFacade;

    @GetMapping("/{scheduleId}/attendance")
    public AttendanceDetailResponse getAttendanceDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable Long storeId,
                                                        @PathVariable Long scheduleId){
        final Long userId = userDetails.getUserId();
        return bossAttendanceFacade.getAttendanceDetail(storeId, userId, scheduleId);
    }
}
