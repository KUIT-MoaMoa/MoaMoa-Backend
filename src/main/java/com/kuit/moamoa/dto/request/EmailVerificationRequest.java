package com.kuit.moamoa.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmailVerificationRequest {
    private String userMail;
}
