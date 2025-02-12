package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.request.chat.CreateUserGroupRequest;
import com.kuit.moamoa.dto.request.chat.InviteUserRequest;
import com.kuit.moamoa.dto.request.chat.UpdateUserGroupRequest;
import com.kuit.moamoa.dto.response.chat.InviteUserResponse;
import com.kuit.moamoa.dto.response.chat.UserGroupResponse;
import com.kuit.moamoa.global.exception.ChatException;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.global.response.ErrorResponse;
import com.kuit.moamoa.service.UserGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user-groups")
public class UserGroupController {

    private final UserGroupService userGroupService;

    /**
     * 채팅방 생성
     */
    @PostMapping("/create")
    public ApiResponse<UserGroupResponse> createUserGroup(
            @Valid @RequestBody CreateUserGroupRequest request) {

        UserGroupResponse response = userGroupService.createUserGroup(request);
        return new ApiResponse<>(response);
    }

    /**
     * 채팅방 이름 변경
     */
    @PutMapping("/{userGroupId}")
    public ApiResponse<UserGroupResponse> updateUserGroup(
            @PathVariable Long userGroupId, @RequestBody UpdateUserGroupRequest request) {

        UserGroupResponse response = userGroupService.updateUserGroup(userGroupId, request);
        return new ApiResponse<>(response);
    }

    /**
     * 채팅방 나가기
     */
    @DeleteMapping("/{userGroupId}/users/{userId}")
    public ApiResponse<Void> leaveUserGroup(
            @PathVariable Long userGroupId, @PathVariable Long userId) {

        userGroupService.leaveUserGroup(userGroupId, userId);
        return new ApiResponse<>(null);
    }

    /**
     * 특정 유저가 속한 채팅방 목록 조회
     */
    @GetMapping("/users/{userId}")
    public ApiResponse<List<UserGroupResponse>> getUserGroupsByUserId(@PathVariable Long userId) {

        List<UserGroupResponse> responses = userGroupService.getUserGroupsByUserId(userId);
        return new ApiResponse<>(responses);
    }

    // 채팅방에 사용자를 초대하는 API
    @PostMapping("/{userGroupId}/invite")
    public ApiResponse<InviteUserResponse> inviteUsers(
            @PathVariable Long userGroupId,
            @Valid @RequestBody InviteUserRequest request) {

        InviteUserResponse response = userGroupService.inviteUsersToGroup(userGroupId, request.getUserIds());
        return new ApiResponse<>(response);
    }
}
