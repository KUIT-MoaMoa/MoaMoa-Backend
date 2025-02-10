package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.AdornProfileResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
}
