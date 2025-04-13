package com.mangoboss.app.common.exception;

import lombok.Getter;

@Getter
public enum CustomErrorInfo {

    // 400 Bad Request
    LOGIN_FAILED(400, "아이디 또는 비밀번호가 잘못되었습니다.", 400001),
    DUPLICATED_EMAIL(400, "이미 존재하는 이메일입니다.", 400002),

    // 401 Unauthorized
    LOGIN_NEEDED(401, "로그인이 필요합니다.", 401001),
    UNAUTHORIZED(401, "인증에 실패하였습니다.", 401002),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다.", 401003),
    EXPIRED_TOKEN(401, "토큰이 만료되었습니다.", 401004),
    UNSUPPORTED_TOKEN(401, "지원되지 않는 토큰입니다.", 401005),
    ILLEGAL_ARGUMENT_TOKEN(401, "값 자체가 잘못 되었습니다.", 401006),

    // 403 FORBIDDEN

    // 404 Not Found
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다.", 404001),
    URL_INPUT_ERROR(404, "URL 입력 오류입니다.", 404002),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(405, "HTTP 메서드가 잘못되었습니다.", 405001);

    // 409 CONFLICT

    // 500 INTERNAL_SERVER_ERROR

    private final int statusCode;
    private final String message;
    private final int detailStatusCode;

    CustomErrorInfo(final int statusCode, final String message, final int detailStatusCode) {
        this.statusCode = statusCode;
        this.message = message;
        this.detailStatusCode = detailStatusCode;
    }
}
