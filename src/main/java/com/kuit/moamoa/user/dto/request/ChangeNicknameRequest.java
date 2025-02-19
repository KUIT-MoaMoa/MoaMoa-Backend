package com.kuit.moamoa.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeNicknameRequest {
    String newNickname;
}
