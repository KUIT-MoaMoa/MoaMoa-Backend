package com.kuit.moamoa.dto.request.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class InviteUserRequest {
    private List<Long> userIds;

    public InviteUserRequest(List<Long> userIds) {
        this.userIds = userIds;
    }
}

