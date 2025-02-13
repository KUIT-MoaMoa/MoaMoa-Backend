package com.kuit.moamoa.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NicknameRequest {

    private String email;
    private String nickname;
}
