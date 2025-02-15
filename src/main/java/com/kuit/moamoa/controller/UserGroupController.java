package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.request.chat.CreateUserGroupRequest;
import com.kuit.moamoa.dto.request.chat.InviteUserRequest;
import com.kuit.moamoa.dto.request.chat.UpdateUserGroupRequest;
import com.kuit.moamoa.dto.response.challenge.UserOngoingChallengeResponse;
import com.kuit.moamoa.dto.response.chat.GroupChallengeHistoryResponse;
import com.kuit.moamoa.dto.response.chat.InviteUserResponse;
import com.kuit.moamoa.dto.response.chat.UserGroupResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
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
public class UserGroupController {  // TODO: pathvariable -> Jwt

    private final UserGroupService userGroupService;

    /**
     * 채팅방 생성
     */
    @PostMapping("/create")
    public ApiResponse<UserGroupResponse> createUserGroup(
            @Valid @RequestBody CreateUserGroupRequest request) {

        log.info("Creating User Group: {}", request);
        UserGroupResponse response = userGroupService.createUserGroup(request);
        return new ApiResponse<>(response);
    }

    /**
     * 채팅방 이름 변경
     */
    @PutMapping("/{userGroupId}")
    public ApiResponse<UserGroupResponse> updateUserGroup(
            @PathVariable("userGroupId") Long userGroupId, @RequestBody UpdateUserGroupRequest request) {

        log.info("Updating User Group: userGroupId={}, request={}", userGroupId, request);
        UserGroupResponse response = userGroupService.updateUserGroup(userGroupId, request);
        return new ApiResponse<>(response);
    }

    /**
     * 채팅방 나가기
     */
    @DeleteMapping("/{userGroupId}/users/{userId}")
    public ApiResponse<Void> leaveUserGroup(
            @PathVariable("userGroupId") Long userGroupId, @PathVariable("userId") Long userId) {

        log.info("User leaving User Group: userId={}, userGroupId={}", userId, userGroupId);
        userGroupService.leaveUserGroup(userGroupId, userId);
        return new ApiResponse<>(null);
    }

    /**
     * 특정 유저가 속한 채팅방 목록 조회
     */
    @GetMapping("/users/{userId}")
    public ApiResponse<List<UserGroupResponse>> getUserGroupsByUserId(@PathVariable("userId") Long userId) {

        log.info("Fetching User Groups for userId={}", userId);
        List<UserGroupResponse> responses = userGroupService.getUserGroupsByUserId(userId);
        return new ApiResponse<>(responses);
    }

    // 채팅방에 사용자를 초대하는 API
    @PostMapping("/{userGroupId}/invite")
    public ApiResponse<InviteUserResponse> inviteUsers(
            @PathVariable("userGroupId") Long userGroupId,
            @Valid @RequestBody InviteUserRequest request) {

        InviteUserResponse response = userGroupService.inviteUsersToGroup(userGroupId, request.getUserIds());
        return new ApiResponse<>(response);
    }

    // 해당 채팅방에서 진행한 챌린지 목록
    @GetMapping("/{groupId}/challenges/history")
    public ApiResponse<List<GroupChallengeHistoryResponse>> getGroupChallengeHistory(
            @PathVariable Long groupId,
            @Jwt Long userId) {

        List<GroupChallengeHistoryResponse> responses = userGroupService.getGroupChallengeHistory(groupId, userId);
        return new ApiResponse<>(responses);
    }


     //해당 채팅방에서 진행 중인 챌린지 목록
    @GetMapping("/{groupId}/challenges/ongoing")
    public ApiResponse<List<UserOngoingChallengeResponse>> getGroupOngoingChallenges(
            @PathVariable Long groupId) {

        log.info("Fetching ongoing challenges for userGroupId={}", groupId);
        List<UserOngoingChallengeResponse> responses = userGroupService.getGroupOngoingChallenges(groupId);
        return new ApiResponse<>(responses);
    }
}
