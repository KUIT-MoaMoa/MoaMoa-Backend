package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.*;
import com.kuit.moamoa.dto.request.chat.CreateUserGroupRequest;
import com.kuit.moamoa.dto.request.chat.UpdateUserGroupRequest;
import com.kuit.moamoa.dto.response.chat.UserGroupResponse;
import com.kuit.moamoa.global.exception.ChatException;
import com.kuit.moamoa.global.exception.ErrorCode;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.repository.UserGroupRepository;
import com.kuit.moamoa.repository.UserRepository;
import com.kuit.moamoa.repository.UserUserGroupJunctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserGroupService {

    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final UserUserGroupJunctionRepository userUserGroupJunctionRepository;


    //채팅방 생성
    @Transactional
    public ApiResponse<UserGroupResponse> createUserGroup(CreateUserGroupRequest request) {
        // 채팅방 생성
        UserGroup userGroup = new UserGroup(request.getTitle());
        userGroup.setStatus(Status.ACTIVE);  // Status 설정 추가
        userGroupRepository.save(userGroup);

        // 요청된 사용자들 조회
        List<User> users = userRepository.findAllById(request.getUserIds());
        if (users.size() != request.getUserIds().size()) {
            throw new ChatException(ErrorCode.USER_NOT_FOUND, "Some users not found");
        }

        // 사용자들을 채팅방에 추가
        for (User user : users) {
            UserUserGroupJunction junction = new UserUserGroupJunction(user, userGroup);
            junction.setStatus(Status.ACTIVE);  // Status 설정 추가
            userUserGroupJunctionRepository.save(junction);
        }

        return new ApiResponse<>(UserGroupResponse.from(userGroup, null));
    }


    //채팅방 이름 변경
    public ApiResponse<UserGroupResponse> updateUserGroup(Long userGroupId, UpdateUserGroupRequest request) {
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new ChatException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + userGroupId));

        userGroup.updateTitle(request.getTitle());
        userGroupRepository.save(userGroup);

        return new ApiResponse<>(UserGroupResponse.from(userGroup, null));
    }


    //채팅방 나가기
    public ApiResponse<Void> leaveUserGroup(Long userGroupId, Long userId) {
        UserUserGroupJunction junction = userUserGroupJunctionRepository
                .findByUserIdAndUserGroupId(userId, userGroupId)
                .orElseThrow(() -> new ChatException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + userGroupId));

        userUserGroupJunctionRepository.delete(junction);

        return new ApiResponse<>(null);
    }


    //특정 유저가 속한 채팅방 목록 조회
    @Transactional(readOnly = true)
    public ApiResponse<List<UserGroupResponse>> getUserGroupsByUserId(Long userId) {
        // 유저 존재 확인
        userRepository.findById(userId)
                .orElseThrow(() -> new ChatException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + userId
                ));

        // 채팅방 목록과 최근 채팅 조회
        List<Object[]> results = userUserGroupJunctionRepository.findUserGroupsWithLastChat(userId);

        // 응답 변환
        List<UserGroupResponse> responses = results.stream()
                .map(result -> {
                    UserGroup userGroup = (UserGroup) result[0];
                    Object chatObject = result[1];  // Chat이 아닐 수도 있음
                    Chat lastChat = (chatObject instanceof Chat) ? (Chat) chatObject : null;  // 안전한 캐스팅
                    return UserGroupResponse.from(userGroup, lastChat);
                })
                .collect(Collectors.toList());


        return new ApiResponse<>(responses);
    }


    // 채팅방에 친구 초대
    public ApiResponse<List<Long>> inviteUsersToGroup(Long userGroupId, List<Long> userIds) {
        // UserGroup 조회
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new ChatException(ErrorCode.USER_GROUP_NOT_FOUND,
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
                        throw new ChatException(ErrorCode.USER_ALREADY_IN_GROUP,
                                "User is already in the group: " + user.getId());
                    }

                    // 그룹에 사용자 추가
                    UserUserGroupJunction junction = new UserUserGroupJunction(user, userGroup);
                    userUserGroupJunctionRepository.save(junction);

                    return user.getId();
                })
                .collect(Collectors.toList());

        return new ApiResponse<>(invitedUserIds);
    }

}