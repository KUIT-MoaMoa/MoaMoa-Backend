package com.kuit.moamoa.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class ResetPasswordRequest {
    private String password;
}
