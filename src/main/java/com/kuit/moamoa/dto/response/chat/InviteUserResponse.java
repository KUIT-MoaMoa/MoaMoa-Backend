package com.kuit.moamoa.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InviteUserResponse {
    private Long userId;
    private String nickname;
    private String message;
}
