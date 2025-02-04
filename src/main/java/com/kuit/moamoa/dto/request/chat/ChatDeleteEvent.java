package com.kuit.moamoa.dto.request.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDeleteEvent {
    private Long chatId;
    private String type = "DELETE";

    public ChatDeleteEvent(Long chatId) {
        this.chatId = chatId;
    }
}
