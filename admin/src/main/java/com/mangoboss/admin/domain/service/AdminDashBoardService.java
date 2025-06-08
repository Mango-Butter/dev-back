package com.mangoboss.admin.domain.service;

import com.mangoboss.admin.domain.repository.StaffRepository;
import com.mangoboss.admin.domain.repository.StoreRepository;
import com.mangoboss.admin.domain.repository.SubscriptionRepository;
import com.mangoboss.admin.domain.repository.UserRepository;
import com.mangoboss.admin.dto.dashboard.*;
import com.mangoboss.storage.store.StoreType;
import com.mangoboss.storage.subscription.PlanTypeCountProjection;
import com.mangoboss.storage.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDashBoardService {

    private final StoreRepository storeRepository;
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public UserStatisticsResponse getUserStatisticsByPeriod(final LocalDate startDate, final LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();

        final Long bossCount = userRepository.countByRoleAndCreatedAtBetween(Role.BOSS, start, end);
        final Long storeCount = storeRepository.countByCreatedAtBetween(start, end);
        final Long totalUserCount = userRepository.countByCreatedAtBetween(start, end);
        final Long staffCount = staffRepository.countByCreatedAtBetween(start, end);
        final Long storeTypeCount = (long) StoreType.values().length;

        return UserStatisticsResponse.of(bossCount, storeCount, totalUserCount, staffCount, storeTypeCount);
    }

    public List<StoreTypeStatisticsResponse> getStoreTypeStatisticsByPeriod(final LocalDate startDate, final LocalDate endDate) {
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();

        List<StoreTypeStatisticsResponse> storeTypeStatisticsList = new ArrayList<>();
        for (StoreType type : StoreType.values()) {
            Long storeCount = storeRepository.countByStoreTypeAndCreatedAtBetween(type, start, end);
            storeTypeStatisticsList.add(StoreTypeStatisticsResponse.of(type, storeCount));
        }
        return storeTypeStatisticsList;
    }

    public List<BossStatisticsResponse> getBossStatistics() {
        return userRepository.findByRole(Role.BOSS).stream()
                .map(boss -> {
                    final Long userId = boss.getId();
                    final Long storeCount = storeRepository.countByBossId(userId);
                    final Long staffCount = staffRepository.countByUserId(userId);
                    return BossStatisticsResponse.of(boss.getName(), storeCount, staffCount);
                }).collect(Collectors.toList());
    }

    public SubscriptionStatisticsResponse getSubscriptionStatistics() {
        Long totalCount = subscriptionRepository.countTotalSubscriptions();
        Long activeCount = subscriptionRepository.countActiveSubscriptions();
        Long inactiveCount = totalCount - activeCount;

        List<PlanTypeCountProjection> rawPlanCounts = subscriptionRepository.countActiveSubscriptionsByPlanType();
        List<SubscriptionStatisticsResponse.PlanTypeCount> planTypeCounts =
                SubscriptionStatisticsResponse.convert(rawPlanCounts);

        return SubscriptionStatisticsResponse.of(totalCount, activeCount, inactiveCount, planTypeCounts);
    }
}