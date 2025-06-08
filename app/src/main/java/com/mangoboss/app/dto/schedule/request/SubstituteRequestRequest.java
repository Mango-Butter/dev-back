package com.mangoboss.app.dto.schedule.request;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record SubstituteRequestRequest(
        @NonNull
        Long targetStaffId,

        @NotBlank
        String reason
){
}
