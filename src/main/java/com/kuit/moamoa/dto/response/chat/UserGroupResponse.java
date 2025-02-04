package com.kuit.moamoa.dto.response.chat;

import com.kuit.moamoa.domain.UserGroup;
import lombok.Getter;

@Getter
public class UserGroupResponse {
    private final Long id;
    private final String title;

    public UserGroupResponse(UserGroup userGroup) {
        this.id = userGroup.getId();
        this.title = userGroup.getTitle();
    }

    public static UserGroupResponse from(UserGroup userGroup) {
        return new UserGroupResponse(userGroup);
    }
}

