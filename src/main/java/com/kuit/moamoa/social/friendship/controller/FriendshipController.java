package com.kuit.moamoa.social.friendship.controller;


import com.kuit.moamoa.global.notification.dto.FriendRequestActionRequest;
import com.kuit.moamoa.social.friendship.dto.SearchUserResponse;
import com.kuit.moamoa.configuration.response.ApiResponse;
import com.kuit.moamoa.global.jwt.Jwt;
import com.kuit.moamoa.social.friendship.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @GetMapping("/search")
    public ApiResponse<List<SearchUserResponse>> searchUsers(
            @RequestParam("nickname") String nickname,
            @Jwt Long userId) {
        List<SearchUserResponse> users = friendshipService.searchUsersByNickname(nickname, userId);

        return new ApiResponse<>(users);
    }

    @PostMapping("/request/{toUserId}")
    public ApiResponse<String> requestFriendship(
            @PathVariable("toUserId") Long ToUserId,
            @Jwt Long userId) {
        friendshipService.createFriendshipRequest(userId, ToUserId);

        return new ApiResponse<>("친구 요청이 전송되었습니다");
    }

    @GetMapping("/my-friend")
    public ApiResponse<List<SearchUserResponse>> getAllFriends(@Jwt Long userId) {
        List<SearchUserResponse> friends = friendshipService.getAllFriends(userId);
        return new ApiResponse<>(friends);
    }

    @PostMapping("/request/{notificationId}/action")
    public ApiResponse<String> handleFriendRequest(
            @PathVariable("notificationId") Long notificationId,
            @RequestBody FriendRequestActionRequest request,
            @Jwt Long userId) {

        friendshipService.handleFriendRequest(notificationId, userId, request.isAccept());

        return new ApiResponse<>(request.isAccept() ?
                "친구 요청이 수락되었습니다" :
                "친구 요청이 거절되었습니다");
    }
}