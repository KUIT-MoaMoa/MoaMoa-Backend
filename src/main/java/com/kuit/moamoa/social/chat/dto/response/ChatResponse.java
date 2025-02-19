package com.kuit.moamoa.social.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private Long loginUserId;                   // 로그인한 유저의 ID
    private List<ChatMessageResponse> messages;  // 채팅 메시지 리스트
}
