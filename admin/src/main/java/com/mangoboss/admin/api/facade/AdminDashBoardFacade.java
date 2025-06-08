package com.mangoboss.admin.api.facade;

import com.mangoboss.admin.domain.service.AdminDashBoardService;
import com.mangoboss.admin.dto.dashboard.UserStatisticsResponse;
import com.mangoboss.admin.dto.dashboard.BossStatisticsResponse;
import com.mangoboss.admin.dto.dashboard.StoreTypeStatisticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminDashBoardFacade {

    private final AdminDashBoardService adminDashBoardService;

    public UserStatisticsResponse getUserStatisticsByPeriod(final LocalDate startDate, final LocalDate endDate) {
        return adminDashBoardService.getUserStatisticsByPeriod(startDate, endDate);
    }

    public List<StoreTypeStatisticsResponse> getStoreTypeStatisticsByPeriod(final LocalDate startDate, final LocalDate endDate) {
        return adminDashBoardService.getStoreTypeStatisticsByPeriod(startDate, endDate);
    }

    public List<BossStatisticsResponse> getBossStatistics() {
        return adminDashBoardService.getBossStatistics();
    }
}
