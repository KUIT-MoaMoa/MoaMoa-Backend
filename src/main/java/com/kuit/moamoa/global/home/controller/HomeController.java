package com.kuit.moamoa.global.home.controller;

import com.kuit.moamoa.global.home.dto.HomeResponse;
import com.kuit.moamoa.configuration.response.ApiResponse;
import com.kuit.moamoa.global.jwt.Jwt;
import com.kuit.moamoa.global.home.service.HomeService;
import com.kuit.moamoa.user.service.InvitationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;
    private final InvitationService invitationService;

    @GetMapping("")
    public ApiResponse<HomeResponse> getOverallSummary(@Jwt Long userId, HttpServletRequest request) throws Exception {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies) {
            if(cookie.getName().equals("invitation_nickname")) {
                String userNickname = cookie.getValue();
                cookie.setMaxAge(0);
                log.info(userNickname);
                invitationService.makeFriendship(userId, userNickname);
            }
        }
        return new ApiResponse<>(homeService.getOverallSummary(userId));
    }

    @PostMapping("over-consumption")
    public ApiResponse<Object> receiveOverConsumption(@Jwt Long userId) throws Exception {
        homeService.checkOverConsumptionTest(userId);
        return new ApiResponse<>(null);
    }
}
