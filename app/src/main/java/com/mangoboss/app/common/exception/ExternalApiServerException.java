package com.mangoboss.app.common.exception;

public class ExternalApiServerException extends RuntimeException{
    public ExternalApiServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
