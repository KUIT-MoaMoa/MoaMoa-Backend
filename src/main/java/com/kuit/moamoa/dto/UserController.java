package com.kuit.moamoa.dto;

import com.kuit.moamoa.controller.BuyItemRequest;
import com.kuit.moamoa.controller.UserPageResponse;
import com.kuit.moamoa.dto.AdornProfileResponse;
import com.kuit.moamoa.dto.BuyItemResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/adorn-profile")
    public AdornProfileResponse adornProfile(@Jwt Long userId) throws Exception {
        return userService.lookUpItems(userId);
    }

    @PostMapping("/item")
    public BuyItemResponse butItem(@Jwt Long userId, BuyItemRequest buyItemRequest) throws Exception {
        return userService.buyItem(userId, buyItemRequest.getItemId(), buyItemRequest.getItemName(),
                buyItemRequest.getPrice());
    }

    @GetMapping("")
    public UserPageResponse getUserInfo(@Jwt Long userId) throws Exception {
        return userService.getUserInfo(userId);
    }
}
