package com.mangoboss.app.api.controller.workreport;

import com.mangoboss.app.api.facade.workreport.BossWorkReportFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.workreport.response.WorkReportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/boss/stores/{storeId}/work-reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossWorkReportController {

    private final BossWorkReportFacade bossWorkReportFacade;

    @GetMapping
    public ListWrapperResponse<WorkReportResponse> getWorkReportsByDate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PathVariable Long storeId,
                                                                        @RequestParam LocalDate date) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossWorkReportFacade.getWorkReportsByDate(storeId, userId, date));
    }

    @GetMapping("/{workReportId}")
    public WorkReportResponse getWorkReportDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @PathVariable Long storeId,
                                                  @PathVariable Long workReportId) {
        final Long userId = userDetails.getUserId();
        return bossWorkReportFacade.getWorkReportDetail(storeId, userId, workReportId);
    }
}