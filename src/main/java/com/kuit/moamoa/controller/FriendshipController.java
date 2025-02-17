package com.kuit.moamoa.controller;


import com.kuit.moamoa.dto.response.friend.SearchUserResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.FriendshipService;
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

        return new ApiResponse<>("success");
    }
}