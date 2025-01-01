package com.kuit.moamoa.global.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final int code;
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorResponse(ExceptionResponseStatus ers) {
        this.code = ers.getCode();
        this.status = ers.getStatus();
        this.message = ers.getMessage();
        this.timestamp = LocalDateTime.now();
    }
}
