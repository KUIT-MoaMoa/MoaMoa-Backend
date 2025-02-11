package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.request.challenge.ChallengeCreateRequest;
import com.kuit.moamoa.dto.response.challenge.ChallengeCreateResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/challenges")
public class ChallengeController {
    private final ChallengeService challengeService;

    @PostMapping("/create")
    public ApiResponse<ChallengeCreateResponse> createChallenge(
            @Valid @RequestBody ChallengeCreateRequest request) {

        log.info("챌린지 생성 요청: {}", request);
        ChallengeCreateResponse response = challengeService.createChallenge(request);
        return new ApiResponse<>(response);
    }
}