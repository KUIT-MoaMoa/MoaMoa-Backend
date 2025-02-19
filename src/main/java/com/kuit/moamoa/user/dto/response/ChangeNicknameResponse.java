package com.kuit.moamoa.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeNicknameResponse {
    boolean duplicated;
    String newNickname;
}
