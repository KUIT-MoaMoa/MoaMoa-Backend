package com.kuit.moamoa.global.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestActionRequest {
    @JsonProperty("accept")  // JSON 필드명을 명시적으로 지정
    private Boolean request;   // primitive boolean 대신 Boolean 객체 사용
}