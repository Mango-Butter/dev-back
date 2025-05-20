package com.mangoboss.app.api.controller.calendar;

import com.mangoboss.app.api.facade.calendar.BossCalendarFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.calender.WorkWithStaffResponse;
import com.mangoboss.app.dto.calender.WorkDotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/boss/stores/{storeId}/calendar")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossCalendarController {
    private final BossCalendarFacade bossCalendarFacade;

    @GetMapping
    public ListWrapperResponse<WorkDotResponse> getWorkDots(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Long storeId, @RequestParam LocalDate start, @RequestParam LocalDate end) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossCalendarFacade.getWorkDots(storeId, userId, start, end));
    }

    @GetMapping("/daily")
    public ListWrapperResponse<WorkWithStaffResponse> getDailyWork(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @PathVariable Long storeId, @RequestParam LocalDate date) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossCalendarFacade.getDailyWorks(storeId, userId, date));
    }
}
