package com.mangoboss.app.dto.task.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mangoboss.storage.task.TaskLogVerificationType;
import com.mangoboss.storage.task.TaskRoutineRepeatType;

import java.time.LocalDate;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "taskRoutineRepeatType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DailyTaskRoutineCreateRequest.class, name = "DAILY"),
        @JsonSubTypes.Type(value = WeeklyTaskRoutineCreateRequest.class, name = "WEEKLY"),
        @JsonSubTypes.Type(value = MonthlyTaskRoutineCreateRequest.class, name = "MONTHLY")
})
public interface TaskRoutineBaseRequest {
    TaskRoutineRepeatType taskRoutineRepeatType();
    String title();
    String description();
    LocalDate startDate();
    LocalDate endDate();
    TaskLogVerificationType verificationType();
    String referenceImageFileKey();
}