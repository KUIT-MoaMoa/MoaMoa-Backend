package com.kuit.moamoa.dto.response;

import com.kuit.moamoa.domain.Chat;
import com.kuit.moamoa.domain.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JoinResponse {

    private Long userId;
    private String nickname;

    public static JoinResponse from(User user) {
        return JoinResponse.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .build();
    }
}
