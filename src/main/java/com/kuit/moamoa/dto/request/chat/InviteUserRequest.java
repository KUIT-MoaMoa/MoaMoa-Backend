package com.kuit.moamoa.dto.request.chat;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class InviteUserRequest {
    @NotNull(message = "User IDs cannot be null")
    private List<Long> userIds;

    public InviteUserRequest(List<Long> userIds) {
        this.userIds = userIds;
    }
}

