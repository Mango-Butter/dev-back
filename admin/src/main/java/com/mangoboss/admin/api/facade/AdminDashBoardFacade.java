package com.mangoboss.admin.api.facade;

import com.mangoboss.admin.domain.service.AdminDashBoardService;
import com.mangoboss.admin.dto.dashboard.UserStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AdminDashBoardFacade {

    private final AdminDashBoardService adminDashBoardService;

    public UserStatisticsResponse getUserStatisticsByPeriod(final LocalDate startDate, final LocalDate endDate) {
        return adminDashBoardService.getUserStatisticsByPeriod(startDate, endDate);
    }
}
