package com.mangoboss.batch.common.exception;

import lombok.Getter;

@Getter
public enum CustomErrorInfo {
    // 외부 API 에러
    EXTERNAL_API_EXCEPTION("외부 API 실패", true),
    EXTERNAL_API_LOGICAL_FAILURE("응답은 왔으나 처리 실패", false),
    FAILURE_RETRY("재시도 실패", true),

    // 비즈니스 에러
    PDF_GENERATION_FAILED("pdf 생성 실패", true);


    private final String message;
    private final boolean retryable;

    CustomErrorInfo(final String message, final boolean retryable) {
        this.message = message;
        this.retryable = retryable;
    }
}
