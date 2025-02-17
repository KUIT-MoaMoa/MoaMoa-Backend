package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.RecentConsumptionChallengeGoalResponse;
import com.kuit.moamoa.dto.ConsumptionChallengeResponse;
import com.kuit.moamoa.dto.CreateConsumptionChallengeRequest;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.ConsumptionChallengeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/consumption-challenge")
public class ConsumptionChallengeController {
    private final ConsumptionChallengeService consumptionChallengeService;

    @GetMapping("")
    public ApiResponse<RecentConsumptionChallengeGoalResponse> lookUpRecentTargetGoal(@Jwt Long userId)
            throws Exception {
        return new ApiResponse<>(consumptionChallengeService.lookUpRecentTargetGoal(userId));
    }

    @PostMapping("")
    public ApiResponse<ConsumptionChallengeResponse> createConsumptionChallenge(@Jwt Long userId,
                                                                                @RequestBody CreateConsumptionChallengeRequest createConsumptionChallengeRequest)
            throws Exception {
        ConsumptionChallengeResponse consumptionChallengeResponse = consumptionChallengeService.createConsumptionChallenge(
                userId, createConsumptionChallengeRequest);
        return new ApiResponse<>(consumptionChallengeResponse);
    }
}
