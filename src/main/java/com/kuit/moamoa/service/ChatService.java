package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.Chat;
import com.kuit.moamoa.domain.Status;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.domain.UserGroup;
import com.kuit.moamoa.dto.request.chat.ChatDeleteEvent;
import com.kuit.moamoa.dto.request.chat.ChatMessageRequest;
import com.kuit.moamoa.dto.response.chat.ChatMessageResponse;
import com.kuit.moamoa.global.exception.ChatException;
import com.kuit.moamoa.global.exception.ErrorCode;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.repository.ChatRepository;
import com.kuit.moamoa.repository.UserGroupRepository;
import com.kuit.moamoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    //채팅 저장
    public ApiResponse<ChatMessageResponse> saveChat(ChatMessageRequest request) {
        UserGroup userGroup = userGroupRepository.findById(request.getUserGroupId())
                .orElseThrow(() -> new ChatException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + request.getUserGroupId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ChatException(ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + request.getUserId()));

        Chat chat = Chat.builder()
                .content(request.getContent())
                .status(Status.ACTIVE)
                .build();

        chat.setUserGroup(userGroup);
        chat.setUser(user);

        Chat savedChat = chatRepository.save(chat);
        ChatMessageResponse response = ChatMessageResponse.from(savedChat);

        return new ApiResponse<>(response);
    }

    //채팅 BroadCast(STOMP)사용
    public void broadcastMessage(ChatMessageResponse response) {
        messagingTemplate.convertAndSend("/sub/chat/room/" + response.getUserGroupId(), response);
    }

    //채팅 조회
    @Transactional(readOnly = true)
    public ApiResponse<List<ChatMessageResponse>> getChatMessages(Long userGroupId, LocalDateTime since) {
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new ChatException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + userGroupId));

        List<Chat> chats = (since != null)
                ? chatRepository.findRecentMessages(userGroup, Status.ACTIVE, since)
                : chatRepository.findByUserGroupAndStatusOrderByCreatedAtDesc(userGroup, Status.ACTIVE);

        List<ChatMessageResponse> responses = chats.stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());

        return new ApiResponse<>(responses);
    }
}
