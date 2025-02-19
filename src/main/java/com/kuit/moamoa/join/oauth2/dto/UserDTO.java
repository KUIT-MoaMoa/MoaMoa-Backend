package com.kuit.moamoa.join.oauth2.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserDTO {

    private Long id;

    private String role;

    private String nickname;

    private String username;

    private String email;
}
