package com.kuit.moamoa.join.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmailVerificationRequest {
    private String userMail;
}
