package com.mangoboss.app.common.exception;

import static com.mangoboss.app.common.exception.CustomErrorInfo.*;
import static com.mangoboss.app.common.exception.CustomErrorInfo.METHOD_NOT_ALLOWED;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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

import com.mangoboss.app.dto.user.response.CustomErrorResponse;

@RestControllerAdvice
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

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers,
		HttpStatusCode status,
		WebRequest request
	) {
		String path = ((ServletWebRequest) request).getRequest().getRequestURI();
		CustomErrorResponse errorResponse = new CustomErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			"Validation failed: " + ex.getBindingResult().toString(),
			path,
			LocalDateTime.now()
		);
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
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
		CustomErrorResponse customErrorResponse = new CustomErrorResponse(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			ex.getMessage(),
			path,
			LocalDateTime.now()
		);
		return new ResponseEntity<>(customErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}