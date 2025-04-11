package com.mangoboss.app.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomErrorCode {
    UNAUTHORIZED("인증에 실패하였습니다.", HttpStatus.UNAUTHORIZED.value()),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED.value()),
    EXPIRED_TOKEN("토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED.value()),
    UNSUPPORTED_TOKEN("지원되지 않는 토큰입니다.", HttpStatus.UNAUTHORIZED.value()),
    ILLEGAL_ARGUMENT_TOKEN("값 자체가 잘못 되었습니다.", HttpStatus.UNAUTHORIZED.value()),
    LOGIN_NEEDED("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED.value()),
    LOGIN_FAILED("아이디 또는 비밀번호가 잘못되었습니다.", HttpStatus.BAD_REQUEST.value()),
    DUPLICATED_EMAIL("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST.value()),
    USER_NOT_FOUND( "사용자를 찾을 수 없습니다,", HttpStatus.NOT_FOUND.value()),
    METHOD_NOT_ALLOWED("http 메서드가 잘못되었습니다.", HttpStatus.METHOD_NOT_ALLOWED.value()),
    URL_INPUT_ERROR("url 입력 오류입니다", HttpStatus.NOT_FOUND.value());

    private final String message;
    private final int statusCode;

}
