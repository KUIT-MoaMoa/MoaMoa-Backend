package com.kuit.moamoa.dto.request.chat;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreateUserGroupRequest {
    private String title;
    private List<Long> userIds;
}

