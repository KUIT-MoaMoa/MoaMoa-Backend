package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.request.chat.CreateUserGroupRequest;
import com.kuit.moamoa.dto.request.chat.InviteUserRequest;
import com.kuit.moamoa.dto.request.chat.UpdateUserGroupRequest;
import com.kuit.moamoa.dto.response.chat.InviteUserResponse;
import com.kuit.moamoa.dto.response.chat.UserGroupResponse;
import com.kuit.moamoa.global.exception.ChatException;
import com.kuit.moamoa.global.exception.ErrorCode;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.global.response.ErrorResponse;
import com.kuit.moamoa.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user-groups")
public class UserGroupController {

    private final UserGroupService userGroupService;

    /**채팅방 생성 */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserGroupResponse>> createUserGroup(@RequestBody CreateUserGroupRequest request) {
        return ResponseEntity.ok(userGroupService.createUserGroup(request));
    }

    /**채팅방 이름 변경 */
    @PutMapping("/{userGroupId}")
    public ResponseEntity<ApiResponse<UserGroupResponse>> updateUserGroup(
            @PathVariable Long userGroupId, @RequestBody UpdateUserGroupRequest request) {
        return ResponseEntity.ok(userGroupService.updateUserGroup(userGroupId, request));
    }

    /**채팅방 나가기 */
    @DeleteMapping("/{userGroupId}/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> leaveUserGroup(
            @PathVariable Long userGroupId, @PathVariable Long userId) {
        return ResponseEntity.ok(userGroupService.leaveUserGroup(userGroupId, userId));
    }

    /**특정 유저가 속한 채팅방 목록 조회 */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<List<UserGroupResponse>>> getUserGroupsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(userGroupService.getUserGroupsByUserId(userId));
    }

    // 채팅방에 사용자를 초대하는 API
    @PostMapping("/{userGroupId}/invite")
    public ResponseEntity<ApiResponse<List<Long>>> inviteUsers(
            @PathVariable Long userGroupId,
            @RequestBody InviteUserRequest request) {

        // 유효성 검사: 유저 ID 리스트가 비어있지 않다면 처리
        if (request.getUserIds() == null || request.getUserIds().isEmpty()) {
            throw new ChatException(ErrorCode.USER_NOT_FOUND, "User IDs cannot be empty.");
        }

        // 초대된 사용자 ID 목록을 result로 반환
        ApiResponse<List<Long>> response = userGroupService.inviteUsersToGroup(userGroupId, request.getUserIds());

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorResponse> handleChatException(ChatException e) {
        log.error("Chat error occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(new ErrorResponse(e.getErrorCode().getStatus(), e.getMessage()));
    }
}
