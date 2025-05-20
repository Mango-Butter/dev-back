package com.mangoboss.app.api.controller.attendance;

import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.attendance.base.AttendanceBaseRequest;
import com.mangoboss.app.dto.attendance.response.AttendanceDetailResponse;
import com.mangoboss.app.dto.calender.WorkResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.mangoboss.app.api.facade.attendance.StaffAttendanceFacade;
import com.mangoboss.app.common.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/staff/stores/{storeId}/schedules")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffAttendanceController {
    private final StaffAttendanceFacade attendanceFacade;

    @PostMapping("/attendance/clock-in")
    public void clockIn(@AuthenticationPrincipal CustomUserDetails userDetails,
                        @PathVariable Long storeId,
                        @RequestBody @Valid AttendanceBaseRequest request) {
        final Long userId = userDetails.getUserId();
        attendanceFacade.clockIn(storeId, userId, request);
    }

    @PostMapping("/attendance/clock-out")
    public void clockOut(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @PathVariable Long storeId,
                         @RequestBody @Valid AttendanceBaseRequest request) {
        final Long userId = userDetails.getUserId();
        attendanceFacade.clockOut(storeId, userId, request);
    }

    @GetMapping("/today")
    public ListWrapperResponse<WorkResponse> getTodayWorks(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(attendanceFacade.getTodayWorks(storeId, userId));
    }

    @GetMapping("/attendances")
    public ListWrapperResponse<AttendanceDetailResponse> getAttendancesByDateRange(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                   @PathVariable Long storeId,
                                                                                   @RequestParam final LocalDate start,
                                                                                   @RequestParam final LocalDate end) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(attendanceFacade.getAttendancesByDateRange(storeId, userId, start, end));
    }

    @GetMapping("/{scheduleId}/attendance")
    public AttendanceDetailResponse getAttendanceDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                        @PathVariable Long storeId,
                                                        @PathVariable Long scheduleId) {
        final Long userId = userDetails.getUserId();
        return attendanceFacade.getAttendanceDetail(storeId, userId, scheduleId);
    }


}
