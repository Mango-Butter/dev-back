package com.mangoboss.batch.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExceptionLogger {
    public void logAndNotify(Exception e, String context) {
        log.error("에러 발생 - {}: {}", context, e.getMessage(), e);
    }
}
