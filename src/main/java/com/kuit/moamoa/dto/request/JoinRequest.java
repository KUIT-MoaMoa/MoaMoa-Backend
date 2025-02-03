package com.kuit.moamoa.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JoinRequest {

    private String email;
    private String password;
    private String nickname;
}
