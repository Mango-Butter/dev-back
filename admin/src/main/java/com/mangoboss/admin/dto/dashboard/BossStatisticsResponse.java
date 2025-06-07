package com.mangoboss.admin.dto.dashboard;

import lombok.Builder;

@Builder
public record BossStatisticsResponse(
        String bossName,
        Long storeCount,
        Long staffCount
) {
    public static BossStatisticsResponse of(
            final String bossName,
            final Long storeCount,
            final Long staffCount
    ) {
        return BossStatisticsResponse.builder()
                .bossName(bossName)
                .storeCount(storeCount)
                .staffCount(staffCount)
                .build();
    }
}