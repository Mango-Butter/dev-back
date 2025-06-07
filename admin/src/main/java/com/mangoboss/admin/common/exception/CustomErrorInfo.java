package com.mangoboss.admin.common.exception;

import lombok.Getter;

@Getter
public enum CustomErrorInfo {

    // 400 Bad Request
    AUTHORIZATION_CODE_MISSING(400, "Authorization code가 비어있습니다.", 400001),
    KAKAO_USER_INFO_REQUEST_FAILED(400, "카카오 사용자 정보 요청에 실패했습니다.", 400002),
    KAKAO_TOKEN_PARSING_FAILED(400, "카카오 토큰 파싱을 실패했습니다.", 400003),
    KAKAO_TOKEN_RESPONSE_EMPTY(400, "카카오 토큰 응답이 비어 있습니다.", 400004),
    URL_INPUT_ERROR(400, "URL 입력 오류입니다.", 400005),
    ILLEGAL_ARGUMENT_TOKEN(400, "토큰 형식이 잘못되었습니다.", 400006),
    KAKAO_USER_INFO_INCOMPLETE(400, "카카오에서 받아오는 정보 중 일부가 누락되었습니다.", 400007),

    // 401 Unauthorized
    LOGIN_NEEDED(401, "로그인이 필요합니다.", 401001),
    UNAUTHORIZED(401, "인증에 실패하였습니다.", 401002),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다.", 401003),
    EXPIRED_TOKEN(401, "토큰이 만료되었습니다.", 401004),
    UNSUPPORTED_TOKEN(401, "지원되지 않는 토큰입니다.", 401005),

    // 403 FORBIDDEN

    // 422 Unprocessable Entity

    // 404 Not Found
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다.", 404001),
    KAKAO_USER_INFO_NOT_FOUND(404, "카카오 사용자 정보가 없습니다.", 404002),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(405, "HTTP 메서드가 잘못되었습니다.", 405001),

    // 409 CONFLICT
    ALREADY_SIGNED_UP(409, "이미 가입된 사용자입니다.", 409001);

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
