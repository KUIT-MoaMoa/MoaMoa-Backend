package com.kuit.moamoa.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class InviteUserResponse {
    private List<Long> invitedUserIds;
}
