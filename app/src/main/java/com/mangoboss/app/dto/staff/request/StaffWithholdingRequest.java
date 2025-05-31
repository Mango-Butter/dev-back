package com.mangoboss.app.dto.staff.request;

import com.mangoboss.storage.payroll.WithholdingType;
import lombok.NonNull;

public record StaffWithholdingRequest (
        @NonNull
        WithholdingType withholdingType
){
}
