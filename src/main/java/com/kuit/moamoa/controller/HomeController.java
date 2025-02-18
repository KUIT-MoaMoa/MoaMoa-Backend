package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.home.HomeResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.HomeService;
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

    @GetMapping("")
    public ApiResponse<HomeResponse> getOverallSummary(@Jwt Long userId) throws Exception {
        return new ApiResponse<>(homeService.getOverallSummary(userId));
    }

    @PostMapping("over-consumption")
    public ApiResponse<Object> receiveOverConsumption(@Jwt Long userId) throws Exception {
        homeService.checkOverConsumptionTest(userId);
        return new ApiResponse<>(null);
    }
}
