package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.Friendship;
import com.kuit.moamoa.domain.Status;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.response.friend.SearchUserResponse;
import com.kuit.moamoa.global.exception.ErrorCode;
import com.kuit.moamoa.global.exception.GlobalException;
import com.kuit.moamoa.repository.FriendshipRepository;
import com.kuit.moamoa.repository.UserRepository;
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

    public List<SearchUserResponse> searchUsersByNickname(String nickname, Long currentUserId) {
        List<User> users = userRepository.findByNicknameContaining(nickname);
        return users.stream()
                .filter(user -> !user.getId().equals(currentUserId)) // 현재 사용자 제외
                .map(user -> {
                    // Check if they are friends
                    boolean isFriend = isFriend(currentUserId, user.getId());
                    return new SearchUserResponse(
                            user.getId(),
                            user.getNickname(),
                            user.getImageUrl(),
                            isFriend
                    );
                })
                .collect(Collectors.toList());
    }

    private boolean isFriend(Long currentUserId, Long otherUserId) {
        // Check if there is an active friendship between the current user and the other user
        return friendshipRepository.existsByFromUserIdAndToUserIdAndStatus(currentUserId, otherUserId, Status.ACTIVE) ||
                friendshipRepository.existsByFromUserIdAndToUserIdAndStatus(otherUserId, currentUserId, Status.ACTIVE);
    }

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
                .status(Status.INACTIVE)
                .build();

        friendshipRepository.save(friendship);
    }
}