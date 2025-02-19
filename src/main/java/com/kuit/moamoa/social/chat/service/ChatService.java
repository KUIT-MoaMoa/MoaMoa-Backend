package com.kuit.moamoa.social.chat.service;

import com.kuit.moamoa.social.chat.domain.Chat;
import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.social.usergroup.domain.UserGroup;
import com.kuit.moamoa.social.chat.dto.request.ChatMessageRequest;
import com.kuit.moamoa.social.chat.dto.response.ChatMessageResponse;
import com.kuit.moamoa.configuration.exception.GlobalException;
import com.kuit.moamoa.configuration.exception.ErrorCode;
import com.kuit.moamoa.social.chat.repository.ChatRepository;
import com.kuit.moamoa.social.usergroup.repository.UserGroupRepository;
import com.kuit.moamoa.user.repository.UserRepository;
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
    public ChatMessageResponse saveChat(ChatMessageRequest request) {
        UserGroup userGroup = userGroupRepository.findById(request.getUserGroupId())
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + request.getUserGroupId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + request.getUserId()));

        Chat chat = Chat.builder()
                .content(request.getContent())
                .status(Status.ACTIVE)
                .build();

        chat.setUserGroup(userGroup);
        chat.setUser(user);

        Chat savedChat = chatRepository.save(chat);
        return ChatMessageResponse.from(savedChat);
    }

    //채팅 BroadCast(STOMP)사용
    public void broadcastMessage(ChatMessageResponse response) {
        messagingTemplate.convertAndSend("/sub/chat/room/" + response.getUserGroupId(), response);
    }

    //채팅 조회
    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getChatMessages(Long userGroupId, LocalDateTime since) {
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + userGroupId));

        List<Chat> chats = (since != null)
                ? chatRepository.findRecentMessages(userGroup, Status.ACTIVE, since)
                : chatRepository.findByUserGroupAndStatusOrderByCreatedAtDesc(userGroup, Status.ACTIVE);

        return chats.stream()
                .map(ChatMessageResponse::from)
                .collect(Collectors.toList());
    }
}
