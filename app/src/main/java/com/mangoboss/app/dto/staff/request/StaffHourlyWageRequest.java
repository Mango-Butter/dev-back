package com.mangoboss.app.dto.staff.request;

import lombok.NonNull;

public record StaffHourlyWageRequest(
        @NonNull
        Integer hourlyWage
) {
}
