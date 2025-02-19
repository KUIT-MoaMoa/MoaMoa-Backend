package com.kuit.moamoa.social.friendship.service;

import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.global.notification.domain.Notification;
import com.kuit.moamoa.global.notification.domain.NotificationType;
import com.kuit.moamoa.global.notification.service.NotificationService;
import com.kuit.moamoa.social.friendship.dto.SearchUserResponse;
import com.kuit.moamoa.configuration.exception.ErrorCode;
import com.kuit.moamoa.configuration.exception.GlobalException;
import com.kuit.moamoa.social.challenge.repository.ChallengeProgressRepository;
import com.kuit.moamoa.social.friendship.repository.FriendshipRepository;
import com.kuit.moamoa.global.notification.repository.NotificationRepository;
import com.kuit.moamoa.social.friendship.domain.Friendship;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.user.repository.UserRepository;
import com.kuit.moamoa.social.challenge.domain.ChallengeStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendshipService {

    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final ChallengeProgressRepository challengeProgressRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;

    // 친구 검색
    public List<SearchUserResponse> searchUsersByNickname(String nickname, Long currentUserId) {
        List<User> users = userRepository.findByNicknameContaining(nickname);
        return users.stream()
                .filter(user -> !user.getId().equals(currentUserId)) // 현재 사용자 제외
                .map(user -> {
                    boolean isFriend = isFriend(currentUserId, user.getId());
                    return new SearchUserResponse(
                            user.getId(),
                            user.getNickname(),
                            user.getImageUrl(),
                            user.getBoarderUrl(),
                            isFriend,
                            isInSameChallenge(currentUserId, user.getId())
                    );
                })
                .collect(Collectors.toList());
    }

    private boolean isFriend(Long currentUserId, Long otherUserId) {
        return friendshipRepository.existsByFromUserIdAndToUserIdAndStatus(currentUserId, otherUserId, Status.ACTIVE) ||
                friendshipRepository.existsByFromUserIdAndToUserIdAndStatus(otherUserId, currentUserId, Status.ACTIVE);
    }

    // 친구 요청
    @Transactional
    public void createFriendshipRequest(Long fromUserId, Long toUserId) {
        // 자기 자신에게 친구 신청하는 경우
        if (fromUserId.equals(toUserId)) {
            throw new GlobalException(ErrorCode.INVALID_REQUEST, "Cannot send friend request to yourself");
        }

        // 이미 친구 관계인지 확인
        if (friendshipRepository.existsByFromUserIdAndToUserIdAndStatus(fromUserId, toUserId, Status.ACTIVE) ||
                friendshipRepository.existsByFromUserIdAndToUserIdAndStatus(toUserId, fromUserId, Status.ACTIVE)) {
            throw new GlobalException(ErrorCode.ALREADY_FRIENDS, "Users are already friends");
        }

        // 이미 친구 요청을 보낸 경우
        if (friendshipRepository.existsByFromUserIdAndToUserIdAndStatus(fromUserId, toUserId, Status.INACTIVE)) {
            throw new GlobalException(ErrorCode.DUPLICATE_REQUEST, "Friend request already sent");
        }

        userRepository.findById(fromUserId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND, "From user not found"));

        userRepository.findById(toUserId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND, "To user not found"));

        Friendship friendship = Friendship.builder()
                .fromUserId(fromUserId)
                .toUserId(toUserId)
                .status(Status.INACTIVE)  // 요청 상태 (비활성 상태로 시작)
                .build();

        friendshipRepository.save(friendship);

        // 친구 요청 알림 생성
        notificationService.createFriendRequestNotification(toUserId, fromUserId, friendship.getId());
    }

    public List<SearchUserResponse> getAllFriends(Long userId) {
        // 상태가 ACTIVE인 모든 친구 관계 찾기 (from과 to 양쪽 모두 고려)
        List<Friendship> friendships = friendshipRepository.findAllByFromUserIdAndStatusOrToUserIdAndStatus(
                userId, Status.ACTIVE, userId, Status.ACTIVE);

        // 친구 ID 추출
        List<Long> friendIds = friendships.stream()
                .map(f -> f.getFromUserId().equals(userId) ? f.getToUserId() : f.getFromUserId())
                .collect(Collectors.toList());

        // 친구 정보 조회
        List<User> friends = userRepository.findAllById(friendIds);

        // 응답 변환
        return friends.stream()
                .map(user -> new SearchUserResponse(
                        user.getId(),
                        user.getNickname(),
                        user.getImageUrl(),
                        user.getBoarderUrl(),
                        true, // 이미 친구이므로 항상 true
                        isInSameChallenge(userId, user.getId())
                ))
                .collect(Collectors.toList());
    }

    private boolean isInSameChallenge(Long userId1, Long userId2) {
        // 첫 번째 사용자가 참여 중인 RECRUITING 또는 ONGOING 상태의 챌린지 ID 목록 조회
        List<Long> user1ChallengeIds = challengeProgressRepository.findChallengeIdsByUserIdAndChallengeStatus(
                userId1,
                List.of(ChallengeStatus.RECRUITING, ChallengeStatus.ONGOING)
        );

        if (user1ChallengeIds.isEmpty()) {
            return false;
        }

        // 두 번째 사용자가 첫 번째 사용자의 챌린지 중 하나라도 참여하고 있는지 확인
        return challengeProgressRepository.existsByUserIdAndChallengeIdIn(userId2, user1ChallengeIds);
    }

    // 친구 요청 받기
    @Transactional
    public void handleFriendRequest(Long notificationId, Long userId, boolean accept) {
        // 알림 조회
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new GlobalException(ErrorCode.NOTIFICATION_NOT_FOUND, "Notification not found"));

        // 알림이 현재 사용자의 것인지 확인
        if (!notification.getUser().getId().equals(userId)) {
            throw new GlobalException(ErrorCode.INVALID_STATUS, "This notification does not belong to the current user");
        }

        // 알림이 친구 요청인지 확인
        if (notification.getType() != NotificationType.FRIEND_REQUEST) {
            throw new GlobalException(ErrorCode.INVALID_REQUEST, "This notification is not a friend request");
        }

        // 친구 요청(Friendship) 조회
        Long friendshipId = notification.getRelationId();
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new GlobalException(ErrorCode.INVALID_STATUS, "Friendship not found"));

        // 수락/거절 처리
        if (accept) {
            // 친구 요청 수락
            friendship.setStatus(Status.ACTIVE);
            Friendship anotherFriendship = Friendship.builder()
                    .fromUserId(friendship.getToUserId())
                    .toUserId(friendship.getFromUserId())
                    .status(Status.ACTIVE)  // 요청 상태 (비활성 상태로 시작)
                    .build();

            friendshipRepository.save(anotherFriendship);
        } else {
            // 친구 요청 거절 -> INACTIVE로 고정이기에 다시 친구요청을 못보냄
            friendship.setStatus(Status.INACTIVE);
        }

        // 알림 비활성화
        notification.setStatus(Status.INACTIVE);
    }
}