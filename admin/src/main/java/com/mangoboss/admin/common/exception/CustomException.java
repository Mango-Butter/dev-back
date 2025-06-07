package com.mangoboss.admin.common.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final CustomErrorInfo customErrorInfo;

    public CustomException(final CustomErrorInfo customErrorInfo) {
        super(customErrorInfo.getMessage());
        this.customErrorInfo = customErrorInfo;
    }

    public CustomErrorInfo getErrorCode() {
        return customErrorInfo;
    }
}
