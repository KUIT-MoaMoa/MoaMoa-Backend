package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.request.chat.CreateUserGroupRequest;
import com.kuit.moamoa.dto.request.chat.UpdateUserGroupRequest;
import com.kuit.moamoa.dto.response.chat.UserGroupResponse;
import com.kuit.moamoa.global.response.ApiResponse;
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
}
