package com.kuit.moamoa.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder @Getter
public class NicknameRequest {

    private Long userId;
    private String nickname;
}
