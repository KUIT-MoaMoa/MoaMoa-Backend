package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.request.chat.ChatMessageRequest;
import com.kuit.moamoa.dto.request.chat.UpdateChatRequest;
import com.kuit.moamoa.dto.response.chat.ChatMessageResponse;
import com.kuit.moamoa.global.exception.ChatException;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.global.response.ErrorResponse;
import com.kuit.moamoa.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/message")
    public void message(ChatMessageRequest request) {
        log.info("Received chat message: {}", request);
        ApiResponse<ChatMessageResponse> response = chatService.saveChat(request);
        chatService.broadcastMessage(response.getResult());
    }

    @GetMapping("/rooms/{userGroupId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponse>>> getChatMessages(
            @PathVariable Long userGroupId,
            @RequestParam(required = false) LocalDateTime since) {

        return ResponseEntity.ok(chatService.getChatMessages(userGroupId, since));
    }

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorResponse> handleChatException(ChatException e) {
        log.error("Chat error occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode().getStatus(), e.getMessage()));
    }
}
