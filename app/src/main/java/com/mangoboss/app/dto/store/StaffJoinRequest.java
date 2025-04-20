package com.mangoboss.app.dto.store;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record StaffJoinRequest (
        @NotBlank
        String inviteCode
){

}
