package com.mangoboss.app.dto.task.request;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;

public enum TaskRoutineDeleteOption {
    ALL,
    PENDING;

    public static TaskRoutineDeleteOption from(final String value) {
        try {
            return TaskRoutineDeleteOption.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(CustomErrorInfo.INVALID_DELETE_OPTION);
        }
    }
}