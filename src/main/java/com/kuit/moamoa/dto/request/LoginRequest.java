package com.kuit.moamoa.dto.request;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRequest {

    private String password;
    private String email;
}
