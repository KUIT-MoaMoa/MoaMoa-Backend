package com.kuit.moamoa.global.notification.service;

import com.kuit.moamoa.global.notification.dto.NotificationResponse;
import com.kuit.moamoa.configuration.exception.ErrorCode;
import com.kuit.moamoa.configuration.exception.GlobalException;
import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.global.notification.domain.Notification;
import com.kuit.moamoa.global.notification.domain.NotificationType;
import com.kuit.moamoa.global.notification.repository.NotificationRepository;
import com.kuit.moamoa.social.usergroup.repository.UserGroupRepository;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.user.repository.UserRepository;
import com.kuit.moamoa.social.challenge.domain.Challenge;
import com.kuit.moamoa.social.usergroup.domain.UserGroup;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public void createFriendRequestNotification(Long toUserId, Long fromUserId, Long friendshipId) {
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND, "To user not found"));

        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND, "From user not found"));

        Notification notification = Notification.builder()
                .user(toUser)
                .content(fromUser.getNickname() + "님에게 친구 요청이 왔어요!")
                .type(NotificationType.FRIEND_REQUEST)
                .relationId(friendshipId)
                .status(Status.ACTIVE)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void createUserGroupRequestNotification(Long toUserId, UserGroup userGroup) {
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND, "To user not found"));

        userGroupRepository.findById(userGroup.getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_GROUP_NOT_FOUND, "존재하지 않는 그룹입니다"));

        Notification notification = Notification.builder()
                .user(toUser)
                .content(userGroup.getTitle() + "에 초대되었어요!")
                .type(NotificationType.USER_GROUP_INVITATION)
                .relationId(userGroup.getId())
                .status(Status.ACTIVE)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public void createChallengeCompletionNotification(Long userId, Challenge challenge, boolean isSuccess) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND, "User not found"));

        String content;
        if (isSuccess) {
            content = challenge.getTitle() + " 챌린지 성공으로 " + challenge.getBattleCoin() + "코인을 얻었어요!";
        } else {
            content = challenge.getTitle() + " 챌린지를 실패해서 " + challenge.getBattleCoin() + "코인을 잃었어요.";
        }

        Notification notification = Notification.builder()
                .user(user)
                .content(content)
                .type(NotificationType.CHALLENGE_COMPLETION)
                .relationId(challenge.getId())
                .status(Status.ACTIVE)
                .build();

        notificationRepository.save(notification);
    }

    @Transactional
    public List<NotificationResponse> getNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, Status.ACTIVE);

        return notifications.stream().map(notification ->
                new NotificationResponse(
                        notification.getId(),
                        notification.getContent(),
                        notification.getType(),
                        notification.getRelationId(),
                        notification.getCreatedAt()
                )
        ).collect(Collectors.toList());
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOTIFICATION_NOT_FOUND, "Notification not found"));

        notification.setStatus(Status.INACTIVE);
    }
}