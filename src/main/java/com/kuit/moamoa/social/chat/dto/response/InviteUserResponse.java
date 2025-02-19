package com.kuit.moamoa.social.chat.dto.response;

import lombok.Getter;
import java.util.List;

@Getter
public class InviteUserResponse {
    private final List<Long> invitedUserIds;

    public InviteUserResponse(List<Long> invitedUserIds) {
        this.invitedUserIds = invitedUserIds;
    }
}
