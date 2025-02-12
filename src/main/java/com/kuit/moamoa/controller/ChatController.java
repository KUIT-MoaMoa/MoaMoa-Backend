package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.request.chat.ChatMessageRequest;
import com.kuit.moamoa.dto.response.chat.ChatMessageResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/message")
    public void message(@Valid ChatMessageRequest request) {
        log.info("Received chat message: {}", request);
        ChatMessageResponse response = chatService.saveChat(request);

        chatService.broadcastMessage(response);
    }

    @GetMapping("/rooms/{userGroupId}/messages")
    public ApiResponse<List<ChatMessageResponse>> getChatMessages(
            @PathVariable("userGroupId") Long userGroupId,
            @RequestParam(required = false) LocalDateTime since) {
        List<ChatMessageResponse> messages = chatService.getChatMessages(userGroupId, since);

        log.info("Fetching messages for userGroupId={}, since={}", userGroupId, since);
        return new ApiResponse<>(messages);
    }
}
