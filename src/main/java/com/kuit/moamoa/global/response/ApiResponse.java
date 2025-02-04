package com.kuit.moamoa.global.response;

import static com.kuit.moamoa.global.response.ExceptionResponseStatus.SUCCESS;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"code", "status", "message", "timestamp", "result"}) // 필드 순서 지정
public class ApiResponse<T> {

    @JsonProperty("code")
    private final int code;

    @JsonProperty("status")
    private final int status;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("timestamp")
    private final LocalDateTime timestamp;

    @Getter
    @JsonInclude(JsonInclude.Include.ALWAYS)
    @JsonProperty("result")
    private final T result;

    public ApiResponse(T result) {
        this.code = SUCCESS.getCode();
        this.status = SUCCESS.getStatus();
        this.message = SUCCESS.getMessage();
        this.timestamp = LocalDateTime.now();
        this.result = result;
    }
}

