package com.kuit.moamoa.social.usergroup.service;

import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.global.notification.service.NotificationService;
import com.kuit.moamoa.social.challenge.domain.Challenge;
import com.kuit.moamoa.social.challenge.domain.ChallengeProgress;
import com.kuit.moamoa.social.chat.domain.Chat;
import com.kuit.moamoa.social.chat.dto.request.CreateUserGroupRequest;
import com.kuit.moamoa.social.chat.dto.request.UpdateUserGroupRequest;
import com.kuit.moamoa.social.challenge.dto.response.UserOngoingChallengeResponse;
import com.kuit.moamoa.social.chat.dto.response.GroupChallengeHistoryResponse;
import com.kuit.moamoa.social.chat.dto.response.InviteUserResponse;
import com.kuit.moamoa.social.chat.dto.response.UserGroupResponse;
import com.kuit.moamoa.configuration.exception.GlobalException;
import com.kuit.moamoa.configuration.exception.ErrorCode;
import com.kuit.moamoa.social.challenge.repository.ChallengeRepository;
import com.kuit.moamoa.social.chat.repository.ChatRepository;
import com.kuit.moamoa.social.usergroup.repository.UserGroupRepository;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.user.repository.UserRepository;
import com.kuit.moamoa.social.usergroup.repository.UserUserGroupJunctionRepository;
import com.kuit.moamoa.social.usergroup.domain.UserGroup;
import com.kuit.moamoa.social.usergroup.domain.UserUserGroupJunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final UserUserGroupJunctionRepository userUserGroupJunctionRepository;
    private final ChallengeRepository challengeRepository;
    private final NotificationService notificationService;
    private final ChatRepository chatRepository;

    //채팅방 생성
    @Transactional
    public UserGroupResponse createUserGroup(CreateUserGroupRequest request, Long userId) {
        // 채팅방 생성
        UserGroup userGroup = new UserGroup(request.getTitle());
        userGroup.setStatus(Status.ACTIVE);  // Status 설정 추가
        userGroupRepository.save(userGroup);

        // 요청된 사용자들 조회
        List<User> users = userRepository.findAllById(request.getUserIds());
        if (users.size() != request.getUserIds().size()) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND, "Some users not found");
        }

        User loginUser = userRepository.findUserById(userId);

        UserUserGroupJunction junction = new UserUserGroupJunction(loginUser, userGroup);
        junction.setStatus(Status.ACTIVE);  // Status 설정 추가
        userUserGroupJunctionRepository.save(junction);

        // 사용자들을 채팅방에 추가
        for (User user : users) {
            junction = new UserUserGroupJunction(user, userGroup);
            junction.setStatus(Status.ACTIVE);  // Status 설정 추가
            userUserGroupJunctionRepository.save(junction);
        }



        return UserGroupResponse.from(userGroup, null, 0);
    }


    //채팅방 이름 변경
    public UserGroupResponse updateUserGroup(Long userGroupId, UpdateUserGroupRequest request) {
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + userGroupId));

        userGroup.updateTitle(request.getTitle());
        userGroupRepository.save(userGroup);

        return UserGroupResponse.from(userGroup, null, 0);
    }


    //채팅방 나가기
    public void leaveUserGroup(Long userGroupId, Long userId) {
        UserUserGroupJunction junction = userUserGroupJunctionRepository
                .findByUserIdAndUserGroupId(userId, userGroupId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + userGroupId));

        userUserGroupJunctionRepository.delete(junction);
    }

    //특정 유저가 속한 채팅방 목록 조회
    @Transactional(readOnly = true)
    public List<UserGroupResponse> getUserGroupsByUserId(Long userId) {
        // 유저 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + userId
                ));

        // 채팅방 목록과 최근 채팅 조회
        List<Object[]> results = userUserGroupJunctionRepository.findUserGroupsWithLastChat(userId);

        // 응답 변환
        return results.stream()
                .map(result -> {
                    UserGroup userGroup = (UserGroup) result[0];
                    Object chatObject = result[1];
                    Chat lastChat = (chatObject instanceof Chat) ? (Chat) chatObject : null;

                    // 해당 채팅방에서 이 사용자가 읽지 않은 메시지 수 조회
                    long unreadCount = chatRepository.countUnreadMessages(userGroup, user);

                    return UserGroupResponse.from(userGroup, lastChat, unreadCount);
                })
                .distinct()
                .collect(Collectors.toList());
    }

    public List<UserGroup> getUserGroupJoined(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + userId
                ));

        // 채팅방 목록과 최근 채팅 조회
        List<Object[]> results = userUserGroupJunctionRepository.findUserGroupsWithLastChat(userId);
        return results.stream()
                .map(result -> (UserGroup) result[0])
                .toList();
    }


    // 채팅방에 친구 초대
    public InviteUserResponse inviteUsersToGroup(Long userGroupId, List<Long> userIds) {
        // UserGroup 조회
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + userGroupId));

        // 초대할 User 조회
        List<User> users = userRepository.findAllById(userIds);

        // 초대할 사용자 리스트
        List<Long> invitedUserIds = users.stream()
                .map(user -> {
                    // 이미 그룹에 존재하는지 확인
                    boolean alreadyInGroup = userUserGroupJunctionRepository
                            .findByUserIdAndUserGroupId(user.getId(), userGroupId)
                            .isPresent();

                    if (alreadyInGroup) {
                        throw new GlobalException(ErrorCode.USER_ALREADY_IN_GROUP,
                                "User is already in the group: " + user.getId());
                    }

                    // 그룹에 사용자 추가
                    UserUserGroupJunction junction = new UserUserGroupJunction(user, userGroup);
                    junction.setStatus(Status.ACTIVE);
                    userUserGroupJunctionRepository.save(junction);

                    notificationService.createUserGroupRequestNotification(user.getId(), userGroup);

                    return user.getId();
                })
                .collect(Collectors.toList());

        return new InviteUserResponse(invitedUserIds);
    }

    @Transactional(readOnly = true)
    public List<GroupChallengeHistoryResponse> getGroupChallengeHistory(Long groupId, Long userId) {
        // fetch join을 사용한 쿼리로 N+1 문제 해결
        List<Challenge> challenges = challengeRepository.findCompletedChallengesByGroupId(groupId);

        if (challenges.isEmpty()) {
            return Collections.emptyList();
        }

        return challenges.stream()
                .map(challenge -> {
                    Boolean isSuccessful = challenge.getProgressList().stream()
                            .filter(progress -> progress.getUser().getId().equals(userId))
                            .map(ChallengeProgress::isGoalAchieved)
                            .findFirst()
                            .orElse(null);

                    return GroupChallengeHistoryResponse.builder()
                            .challengeId(challenge.getId())
                            .title(challenge.getTitle())
                            .content(challenge.getContent())
                            .startDate(challenge.getStartDate())
                            .endDate(challenge.getEndDate())
                            .battleCoin(challenge.getBattleCoin())
                            .participantCount(challenge.getProgressList().size())
                            .isSuccessful(isSuccessful)
                            .status(challenge.getStatus())  // 상태 정보 추가
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserOngoingChallengeResponse> getGroupOngoingChallenges(Long groupId, Long userId) {
        // 그룹 존재 확인
        userGroupRepository.findById(groupId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + groupId));

        // 해당 그룹의 모집 중 또는 진행 중인 챌린지 조회
        List<Challenge> challenges = challengeRepository.findRecruitingOrOngoingChallengesByGroupId(groupId);

        return challenges.stream()
                .map(challenge -> {
                    // 현재 사용자가 챌린지에 참여 중인지 확인
                    boolean isParticipating = challenge.getProgressList().stream()
                            .anyMatch(progress -> progress.getUser().getId().equals(userId));

                    return UserOngoingChallengeResponse.builder()
                            .challengeId(challenge.getId())
                            .title(challenge.getTitle())
                            .content(challenge.getContent())
                            .publicChallenge(challenge.getPublicChallenge())
                            .startDate(challenge.getStartDate())
                            .endDate(challenge.getEndDate())
                            .duration(challenge.getDuration())
                            .battleCoin(challenge.getBattleCoin())
                            .isParticipating(isParticipating)
                            .participantCount(challenge.getProgressList().size())
                            .status(challenge.getStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer getUserGroupMemberCount(Long groupId) {
        // 그룹 존재 확인
        UserGroup userGroup = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + groupId));

        // 해당 그룹의 멤버 수 반환
        return userGroup.getUserUserGroupJunctions().size();
    }

}