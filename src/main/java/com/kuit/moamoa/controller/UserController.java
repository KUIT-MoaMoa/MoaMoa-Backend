package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.AdornProfileResponse;
import com.kuit.moamoa.dto.BuyItemResponse;
import com.kuit.moamoa.dto.InvitationUrlResponse;
import com.kuit.moamoa.dto.MyChallengeSummaryResponse;
import com.kuit.moamoa.dto.UserPageResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {   // TODO: pathvariable -> Jwt required
    private final UserService userService;

    @GetMapping("/adorn-profile")
    public ApiResponse<AdornProfileResponse> adornProfile(@Jwt Long userId) throws Exception {
        return new ApiResponse<>(userService.lookUpItems(userId));
    }

    @PostMapping("/item")
    public ApiResponse<BuyItemResponse> butItem(@Jwt Long userId, @RequestBody BuyItemRequest buyItemRequest) throws Exception {
        log.info(buyItemRequest.toString());
        return new ApiResponse<>(userService.buyItem(userId, buyItemRequest.getItemId(), buyItemRequest.getItemName(),
                buyItemRequest.getPrice()));
    }

    @GetMapping("")
    public ApiResponse<UserPageResponse> getUserInfo(@Jwt Long userId) throws Exception {
        return new ApiResponse<>(userService.getUserInfo(userId));
    }

    @GetMapping("/my-challenge")
    public ApiResponse<MyChallengeSummaryResponse> getUserChallengeSummary(@Jwt Long userId) throws Exception {
        return new ApiResponse<>(userService.getUserChallengeSummary(userId));
    }

    @GetMapping("/invite")
    public ApiResponse<InvitationUrlResponse> makeInvitation(@Jwt Long userId) throws Exception {
        return new ApiResponse<InvitationUrlResponse>(userService.makeInvitationUrl(userId));
    }
}
