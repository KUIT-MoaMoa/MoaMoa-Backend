package com.kuit.moamoa.join.oauth2.dto;

import com.kuit.moamoa.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserAuthResponse {

    private Long userId;

    public static UserAuthResponse from(User user) {
        return UserAuthResponse.builder()
                .userId(user.getId())
                .build();
    }
}
