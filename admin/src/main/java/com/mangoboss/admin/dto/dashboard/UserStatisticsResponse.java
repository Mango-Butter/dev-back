package com.mangoboss.admin.dto.dashboard;

import lombok.Builder;

@Builder
public record UserStatisticsResponse(
        Long bossCount,
        Long storeCount,
        Long totalUserCount,
        Long staffCount,
        Long storeTypeCount
) {
    public static UserStatisticsResponse of(
            final Long bossCount,
            final Long storeCount,
            final Long totalMemberCount,
            final Long staffCount,
            final Long storeTypeCount
    ) {
        return UserStatisticsResponse.builder()
                .bossCount(bossCount)
                .storeCount(storeCount)
                .totalUserCount(totalMemberCount)
                .staffCount(staffCount)
                .storeTypeCount(storeTypeCount)
                .build();
    }
}