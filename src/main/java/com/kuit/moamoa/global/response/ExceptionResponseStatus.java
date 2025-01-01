package com.kuit.moamoa.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionResponseStatus {
    /**
     * 1000: 성공
     */
    SUCCESS(1000,HttpStatus.OK.value(), "요청에 성공하였습니다."),


    /**
     * 2000: Request 오류
     */
    BAD_REQUEST(2000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 요청입니다."),
    URL_NOT_FOUND(2001, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 URL 입니다.");

    private final int code;
    private final int status;
    private final String message;

}
