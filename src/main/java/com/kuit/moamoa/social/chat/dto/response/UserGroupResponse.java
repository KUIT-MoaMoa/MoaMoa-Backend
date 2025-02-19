package com.kuit.moamoa.social.chat.dto.response;

import com.kuit.moamoa.social.chat.domain.Chat;
import com.kuit.moamoa.social.usergroup.domain.UserGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserGroupResponse {
    private Long id;
    private String title;
    private RecentChatInfo recentChat;
    private long unreadCount; // 내가 안 읽은 채팅 수

    @Getter
    @AllArgsConstructor
    public static class RecentChatInfo {
        private String content;
        private String userName;
        private LocalDateTime createdAt;
    }

    public static UserGroupResponse from(UserGroup userGroup, Chat lastChat, long unreadCount) {
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
                recentChatInfo,
                unreadCount
        );
    }
}

