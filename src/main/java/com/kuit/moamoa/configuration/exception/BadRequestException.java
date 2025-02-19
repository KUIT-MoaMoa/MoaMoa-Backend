package com.kuit.moamoa.configuration.exception;

import com.kuit.moamoa.configuration.response.ExceptionResponseStatus;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{

    private final ExceptionResponseStatus exceptionStatus;

    public BadRequestException(ExceptionResponseStatus exceptionStatus) {
        super(exceptionStatus.getMessage());
        this.exceptionStatus = exceptionStatus;
    }
}
