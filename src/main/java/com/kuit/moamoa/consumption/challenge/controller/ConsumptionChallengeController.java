package com.kuit.moamoa.consumption.challenge.controller;

import com.kuit.moamoa.consumption.record.dto.AddMyConsumptionRequest;
import com.kuit.moamoa.consumption.record.dto.AddMyConsumptionResponse;
import com.kuit.moamoa.consumption.challenge.dto.RecentConsumptionChallengeGoalResponse;
import com.kuit.moamoa.consumption.challenge.dto.ConsumptionChallengeResponse;
import com.kuit.moamoa.consumption.challenge.dto.CreateConsumptionChallengeRequest;
import com.kuit.moamoa.configuration.response.ApiResponse;
import com.kuit.moamoa.global.jwt.Jwt;
import com.kuit.moamoa.consumption.challenge.service.ConsumptionChallengeService;
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

    @GetMapping("/my-consumption")
    public ApiResponse<AddMyConsumptionResponse> myConsumptionGoal(@Jwt Long userId) throws Exception {
        return new ApiResponse<>(consumptionChallengeService.getCurrentAmountLeft(userId));
    }

    @PostMapping("/my-consumption")
    public ApiResponse<Object> addMyConsumption(@Jwt Long userId,
                                                                  @RequestBody AddMyConsumptionRequest addMyConsumptionRequest)
            throws Exception {

        return new ApiResponse<>(consumptionChallengeService.addMyConsumption(userId, addMyConsumptionRequest));
    }
}
