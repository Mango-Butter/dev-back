package com.mangoboss.batch.common.exception;

import lombok.Getter;

@Getter
public enum CustomErrorInfo {
    // 외부 API 에러
    EXTERNAL_API_EXCEPTION(504, "외부 API 실패", 500012, true),
    EXTERNAL_API_LOGICAL_FAILURE(502, "응답은 왔으나 처리 실패", 500011, false),
    FAILURE_RETRY(500, "재시도 실패", 500013, true);

    // 비즈니스 에러



    private final int statusCode;
    private final String message;
    private final int detailStatusCode;
    private final boolean retryable;

    CustomErrorInfo(final int statusCode, final String message, final int detailStatusCode, final boolean retryable) {
        this.statusCode = statusCode;
        this.message = message;
        this.detailStatusCode = detailStatusCode;
        this.retryable = retryable;
    }
}
