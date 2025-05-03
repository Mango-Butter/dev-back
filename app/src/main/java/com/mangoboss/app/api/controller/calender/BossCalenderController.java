package com.mangoboss.app.api.controller.calender;

import com.mangoboss.app.api.facade.calender.BossCalenderFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.calender.WorkDotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/boss/stores/{storeId}/calender")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossCalenderController {
    private final BossCalenderFacade bossTimelineFacade;

    @GetMapping
    public ListWrapperResponse<WorkDotResponse> getWorkDots(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Long storeId, @RequestParam LocalDate start, @RequestParam LocalDate end) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossTimelineFacade.getWorkDots(storeId, userId, start, end));
    }
}
