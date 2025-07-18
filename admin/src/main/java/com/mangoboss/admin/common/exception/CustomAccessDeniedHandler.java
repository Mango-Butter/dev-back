package com.mangoboss.admin.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangoboss.admin.dto.CustomErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

// 접근 권한 없을 때 403 에러
@Component
@Slf4j(topic= "FORBIDDEN_EXCEPTION_HANDLER")
@AllArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.warn("403 접근 거부됨! URI: {}, message: {}", request.getRequestURI(), accessDeniedException.getMessage());

        CustomErrorResponse customErrorResponse = new CustomErrorResponse(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage(), request.getRequestURI(), LocalDateTime.now());

        String responseBody = objectMapper.writeValueAsString(customErrorResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }
}
