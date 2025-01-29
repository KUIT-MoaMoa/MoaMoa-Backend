package com.kuit.moamoa.dto.response;

import com.kuit.moamoa.domain.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private Long chatId;
    private Long userGroupId;
    private Long userId;
    private String userName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ChatMessageResponse from(Chat chat) {
        return ChatMessageResponse.builder()
                .chatId(chat.getId())
                .userGroupId(chat.getUserGroup().getId())
                .userId(chat.getUser().getId())
                .userName(chat.getUser().getNickname())
                .content(chat.getContent())
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .build();
    }
}