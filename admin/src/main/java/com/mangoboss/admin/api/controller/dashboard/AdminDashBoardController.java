package com.mangoboss.admin.api.controller.dashboard;

import com.mangoboss.admin.api.facade.AdminDashBoardFacade;
import com.mangoboss.admin.dto.ListWrapperResponse;
import com.mangoboss.admin.dto.dashboard.UserStatisticsResponse;
import com.mangoboss.admin.dto.dashboard.StoreTypeStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/dashboard")
@PreAuthorize("hasRole('BOSS')")
public class AdminDashBoardController {

    private final AdminDashBoardFacade adminDashBoardFacade;

    @GetMapping("/statistics/overview/period")
    public UserStatisticsResponse getUserStatisticsByPeriod(@RequestParam("startDate") LocalDate startDate,
                                                            @RequestParam("endDate") LocalDate endDate) {
        return adminDashBoardFacade.getUserStatisticsByPeriod(startDate, endDate);
    }

    @GetMapping("/statistics/industry/period")
    public ListWrapperResponse<StoreTypeStatisticsResponse> getStoreTypeStatisticsByPeriod(@RequestParam("startDate") LocalDate startDate,
                                                                                          @RequestParam("endDate") LocalDate endDate) {
        return ListWrapperResponse.of(adminDashBoardFacade.getStoreTypeStatisticsByPeriod(startDate, endDate));
    }
}