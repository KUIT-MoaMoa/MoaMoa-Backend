package com.kuit.moamoa.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_GROUP_NOT_FOUND("존재하지 않는 그룹입니다."),
    USER_NOT_FOUND("존재하지 않는 사용자입니다."),
    CHAT_NOT_FOUND("존재하지 않는 메시지입니다."),
    USER_ALREADY_IN_GROUP("이미 그룹에 있는 사용자입니다.");

    private final String message;

    public int getStatus() {
        return ordinal();
    }
}