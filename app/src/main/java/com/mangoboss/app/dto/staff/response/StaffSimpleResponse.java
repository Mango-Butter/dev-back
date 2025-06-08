package com.mangoboss.app.dto.staff.response;

import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

@Builder
public record StaffSimpleResponse(
        Long staffId,
        String name,
        String profileImageUrl
) {
    public static StaffSimpleResponse fromEntity(final StaffEntity staff){
        return StaffSimpleResponse.builder()
                .staffId(staff.getId())
                .name(staff.getName())
                .profileImageUrl(staff.getProfileImageUrl())
                .build();
    }
}
