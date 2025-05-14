package com.mangoboss.app.common.exception;

import static com.mangoboss.app.common.exception.CustomErrorInfo.*;
import static com.mangoboss.app.common.exception.CustomErrorInfo.METHOD_NOT_ALLOWED;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.C;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.mangoboss.app.dto.auth.response.CustomErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "접근 권한이 없습니다.",
                path,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "인증되지 않은 요청입니다.",
                path,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CustomErrorResponse> handleCustomException(CustomException ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                ex.getErrorCode().getStatusCode(),
                ex.getErrorCode().getMessage(),
                path,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatusCode()));
    }

    @ExceptionHandler(ExternalApiServerException.class)
    public ResponseEntity<CustomErrorResponse> handleExternal(ExternalApiServerException e, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        CustomException ex = new CustomException(EXTERNAL_API_EXCEPTION);
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                ex.getErrorCode().getStatusCode(),
                ex.getErrorCode().getMessage(),
                path,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode().getStatusCode()));

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        final String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        // 에러 메시지를 필드 기준으로 추출
        final String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " 필드는 " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        final CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                path,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "요청 바디를 읽을 수 없습니다. 필수 항목 누락 또는 형식 오류.",
                path,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                METHOD_NOT_ALLOWED.getMessage(),
                path,
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        CustomErrorResponse errorResponse = new CustomErrorResponse(
                URL_INPUT_ERROR.getStatusCode(),
                URL_INPUT_ERROR.getMessage(),
                path,
                LocalDateTime.now()
        );
        return ResponseEntity.status(URL_INPUT_ERROR.getStatusCode()).body(errorResponse);
    }

    //Default exception
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<CustomErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.error("Exception at {}: {}", path, ex.getMessage(), ex);
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
                path,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(customErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}