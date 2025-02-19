package com.kuit.moamoa.configuration.exception_handler;


import static com.kuit.moamoa.configuration.response.ExceptionResponseStatus.URL_NOT_FOUND;

import com.kuit.moamoa.configuration.exception.BadRequestException;
import com.kuit.moamoa.configuration.exception.GlobalException;
import com.kuit.moamoa.configuration.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class, NoHandlerFoundException.class, TypeMismatchException.class})
    public ErrorResponse handle_BadRequest(java.lang.Exception e) {
        log.error("[handle_BadRequest]", e);
        return new ErrorResponse(URL_NOT_FOUND) {
        };
    }

    @ExceptionHandler(GlobalException.class)
    public ErrorResponse handleChatException(GlobalException e) {
        log.error("Chat error occurred: {}", e.getMessage(), e);
        return new ErrorResponse(e.getErrorCode().getStatus(), e.getMessage());
    }
}
