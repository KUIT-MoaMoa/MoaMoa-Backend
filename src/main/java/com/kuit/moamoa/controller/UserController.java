package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.AdornProfileResponse;
import com.kuit.moamoa.dto.BuyItemResponse;
import com.kuit.moamoa.dto.MyChallengeSummaryResponse;
import com.kuit.moamoa.dto.UserPageResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/adorn-profile/{userId}")
    public ApiResponse<AdornProfileResponse> adornProfile(@PathVariable Long userId) throws Exception {
        return new ApiResponse<>(userService.lookUpItems(userId));
    }

    @PostMapping("/item/{userId}")
    public ApiResponse<BuyItemResponse> butItem(@PathVariable Long userId, @RequestBody BuyItemRequest buyItemRequest) throws Exception {
        log.info(buyItemRequest.toString());
        return new ApiResponse<>(userService.buyItem(userId, buyItemRequest.getItemId(), buyItemRequest.getItemName(),
                buyItemRequest.getPrice()));
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserPageResponse> getUserInfo(@PathVariable Long userId) throws Exception {
        return new ApiResponse<>(userService.getUserInfo(userId));
    }

    @GetMapping("/my-challenge/{userId}")
    public ApiResponse<MyChallengeSummaryResponse> getUserChallengeSummary(@PathVariable Long userId) throws Exception {
        return new ApiResponse<>(userService.getUserChallengeSummary(userId));
    }
}
