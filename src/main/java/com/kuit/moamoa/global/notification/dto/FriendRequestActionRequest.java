package com.kuit.moamoa.global.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestActionRequest {
    private Boolean request;   // primitive boolean 대신 Boolean 객체 사용
}