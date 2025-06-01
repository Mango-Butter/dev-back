package com.mangoboss.app.dto.staff.response;

import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

@Builder
public record SubstituteCandidateResponse (
        StaffSimpleResponse staff,
        Boolean valid
){
    public static SubstituteCandidateResponse of(final StaffEntity staff, final Boolean valid) {
        return SubstituteCandidateResponse.builder()
                .staff(StaffSimpleResponse.fromEntity(staff))
                .valid(valid)
                .build();
    }
}
