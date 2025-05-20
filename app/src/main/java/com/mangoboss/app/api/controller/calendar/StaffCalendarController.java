package com.mangoboss.app.api.controller.calendar;

import com.mangoboss.app.api.facade.calendar.StaffCalendarFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.calender.WorkDotResponse;
import com.mangoboss.app.dto.calender.WorkWithStaffResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/staff/stores/{storeId}/calendar")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffCalendarController {
    private final StaffCalendarFacade staffCalendarFacade;

    @GetMapping
    public ListWrapperResponse<WorkDotResponse> getWorkDots(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Long storeId, @RequestParam LocalDate start, @RequestParam LocalDate end) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(staffCalendarFacade.getWorkDots(storeId, userId, start, end));
    }

    @GetMapping("/daily")
    public ListWrapperResponse<WorkWithStaffResponse> getDailyWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @PathVariable Long storeId, @RequestParam LocalDate date) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(staffCalendarFacade.getDailyWorks(storeId, userId, date));
    }
}
