package com.mangoboss.app.api.controller.attendance;

import com.mangoboss.app.api.facade.attendance.BossAttendanceFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.attendance.request.AttendanceManualAddRequest;
import com.mangoboss.app.dto.attendance.request.AttendanceUpdateRequest;
import com.mangoboss.app.dto.attendance.response.AttendanceDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/boss/stores/{storeId}/schedules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossAttendanceController {
    private final BossAttendanceFacade bossAttendanceFacade;

    @GetMapping("/{scheduleId}/attendance")
    public AttendanceDetailResponse getAttendanceDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable Long storeId,
                                                        @PathVariable Long scheduleId) {
        final Long userId = userDetails.getUserId();
        return bossAttendanceFacade.getAttendanceDetail(storeId, userId, scheduleId);
    }

    @PostMapping("/attendance")
    public AttendanceDetailResponse addManualAttendance(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable Long storeId, @RequestBody @Valid AttendanceManualAddRequest request) {
        final Long userId = userDetails.getUserId();
        return bossAttendanceFacade.addManualAttendance(storeId, userId, request);
    }

    @PutMapping("/{scheduleId}/attendance")
    public AttendanceDetailResponse updateAttendance(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @PathVariable Long storeId, @PathVariable Long scheduleId, @RequestBody @Valid AttendanceUpdateRequest request) {
        final Long userId = userDetails.getUserId();
        return bossAttendanceFacade.updateAttendance(storeId, userId, scheduleId, request);
    }

    @DeleteMapping("/{scheduleId}/attendance")
    public void deleteAttendance(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @PathVariable Long storeId, @PathVariable Long scheduleId) {
        final Long userId = userDetails.getUserId();
        bossAttendanceFacade.deleteAttendance(storeId, userId, scheduleId);
    }

    @GetMapping("/staffs/{staffId}/attendances")
    public ListWrapperResponse<AttendanceDetailResponse> getAttendancesByStaffAndDateRange(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                           @PathVariable Long storeId,
                                                                                           @PathVariable Long staffId,
                                                                                           @RequestParam LocalDate start,
                                                                                           @RequestParam LocalDate end) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossAttendanceFacade.getAttendancesByStaffAndDateRange(storeId, staffId, userId, start, end));
    }
}
