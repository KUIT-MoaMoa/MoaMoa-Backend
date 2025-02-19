package com.kuit.moamoa.join.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserAuthRequest {

    private String email;
    private String password;
//    private Status status;

}
