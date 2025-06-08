package com.mangoboss.app.dto.store.response;

import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record StoreRequestedResponse(
    Integer requestedCount,
    List<String> profileImageUrls
){
    public static StoreRequestedResponse of(final Integer requestedCount, final List<StaffEntity> staffs) {
        return StoreRequestedResponse.builder()
                .requestedCount(requestedCount)
                .profileImageUrls(staffs.stream()
                        .map(StaffEntity::getProfileImageUrl)
                        .toList())
                .build();
    }
}
