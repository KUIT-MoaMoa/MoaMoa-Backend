package com.kuit.moamoa.user.service;

import com.kuit.moamoa.social.friendship.domain.Friendship;
import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.global.jwt.Jwt;
import com.kuit.moamoa.social.friendship.repository.FriendshipRepository;
import com.kuit.moamoa.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;

    public void makeFriendship(@Jwt Long userId, String nickname) {
        Long userIdAdd = userRepository.findByNickname(nickname).getId();

        friendshipRepository.save(
                Friendship.builder()
                        .fromUserId(userId)
                        .toUserId(userIdAdd)
                        .status(Status.ACTIVE)
                        .build()
        );
        friendshipRepository.save(
                Friendship.builder()
                        .fromUserId(userIdAdd)
                        .toUserId(userId)
                        .status(Status.ACTIVE)
                        .build()
        );
    }
}
