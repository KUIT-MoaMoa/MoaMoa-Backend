package com.kuit.moamoa.dto.response.chat;

import com.kuit.moamoa.domain.Chat;
import com.kuit.moamoa.domain.UserGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserGroupResponse {
    private Long id;
    private String title;
    private RecentChatInfo recentChat;

    @Getter
    @AllArgsConstructor
    public static class RecentChatInfo {
        private String content;
        private String userName;
        private LocalDateTime createdAt;
    }

    public static UserGroupResponse from(UserGroup userGroup, Chat lastChat) {
        RecentChatInfo recentChatInfo = null;
        if (lastChat != null) {
            recentChatInfo = new RecentChatInfo(
                    lastChat.getContent(),
                    lastChat.getUser().getNickname(),
                    lastChat.getCreatedAt()
            );
        }

        return new UserGroupResponse(
                userGroup.getId(),
                userGroup.getTitle(),
                recentChatInfo
        );
    }
}

