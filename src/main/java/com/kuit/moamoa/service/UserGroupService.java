package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.domain.UserGroup;
import com.kuit.moamoa.domain.UserUserGroupJunction;
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

    /**채팅방 생성 */
    public ApiResponse<UserGroupResponse> createUserGroup(CreateUserGroupRequest request) {
        UserGroup userGroup = new UserGroup(request.getTitle());

        userGroupRepository.save(userGroup);

        List<User> users = userRepository.findAllById(request.getUserIds());

        for (User user : users) {
            UserUserGroupJunction junction = new UserUserGroupJunction(user, userGroup);
            userUserGroupJunctionRepository.save(junction);
        }

        return new ApiResponse<>(UserGroupResponse.from(userGroup));
    }

    /**채팅방 이름 변경 */
    public ApiResponse<UserGroupResponse> updateUserGroup(Long userGroupId, UpdateUserGroupRequest request) {
        UserGroup userGroup = userGroupRepository.findById(userGroupId)
                .orElseThrow(() -> new ChatException(ErrorCode.USER_GROUP_NOT_FOUND,
                "UserGroup not found with id: " + userGroupId));

        userGroup.updateTitle(request.getTitle());
        userGroupRepository.save(userGroup);

        return new ApiResponse<>(UserGroupResponse.from(userGroup));
    }

    /**채팅방 나가기 */
    public ApiResponse<Void> leaveUserGroup(Long userGroupId, Long userId) {
        UserUserGroupJunction junction = userUserGroupJunctionRepository
                .findByUserIdAndUserGroupId(userId, userGroupId)
                .orElseThrow(() -> new ChatException(ErrorCode.USER_GROUP_NOT_FOUND,
                        "UserGroup not found with id: " + userGroupId));

        userUserGroupJunctionRepository.delete(junction);

        return new ApiResponse<>(null);
    }

    /**특정 유저가 속한 채팅방 목록 조회 */
    public ApiResponse<List<UserGroupResponse>> getUserGroupsByUserId(Long userId) {
        //예외 처리: 존재하지 않는 유저 ID 요청 시 에러 발생
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChatException(ErrorCode.USER_NOT_FOUND, "User not found with id: " + userId));

        List<UserGroup> userGroups = userUserGroupJunctionRepository.findUserGroupsByUserId(userId);
        List<UserGroupResponse> responses = userGroups.stream().map(UserGroupResponse::from).collect(Collectors.toList());

        return new ApiResponse<>(responses);
    }
}
