package com.mangoboss.app.dto.store.request;

import jakarta.validation.constraints.NotBlank;

public record StaffJoinRequest (
        @NotBlank
        String inviteCode
){

}
