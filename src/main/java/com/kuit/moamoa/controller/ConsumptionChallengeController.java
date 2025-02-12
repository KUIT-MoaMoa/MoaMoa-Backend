package com.kuit.moamoa.controller;

import com.kuit.moamoa.domain.ConsumptionChallenge;
import com.kuit.moamoa.dto.ConsumptionChallengeResponse;
import com.kuit.moamoa.dto.CreateConsumptionChallengeRequest;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.ConsumptionChallengeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/consumption-challenge")
public class ConsumptionChallengeController {
    ConsumptionChallengeService consumptionChallengeService;

    @PostMapping("")
    public ApiResponse<ConsumptionChallengeResponse> createConsumptionChallenge(@Jwt Long userId,
                                                                                @RequestBody CreateConsumptionChallengeRequest createConsumptionChallengeRequest)
            throws Exception {
        ConsumptionChallengeResponse consumptionChallengeResponse = consumptionChallengeService.createConsumptionChallenge(
                userId, createConsumptionChallengeRequest);
        return new ApiResponse<>(consumptionChallengeResponse);
    }
}
