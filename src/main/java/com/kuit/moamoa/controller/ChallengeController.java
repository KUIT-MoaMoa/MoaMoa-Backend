package com.kuit.moamoa.controller;

import com.kuit.moamoa.domain.ChallengeSortType;
import com.kuit.moamoa.dto.request.challenge.ChallengeCreateRequest;
import com.kuit.moamoa.dto.response.challenge.ChallengeCreateResponse;
import com.kuit.moamoa.dto.response.challenge.CompletedChallengeResponse;
import com.kuit.moamoa.dto.response.challenge.PublicChallengeResponse;
import com.kuit.moamoa.dto.response.challenge.UserOngoingChallengeResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/challenges")
public class ChallengeController {  // TODO: pathvariable -> Jwt
    private final ChallengeService challengeService;

    // 일반 챌린지 생성
    @PostMapping("/create/{userId}")
    public ApiResponse<ChallengeCreateResponse> createChallenge(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody ChallengeCreateRequest request) {

        log.info("일반 챌린지 생성 요청: {}", request);
        ChallengeCreateResponse response = challengeService.createChallenge(request, userId);
        return new ApiResponse<>(response);
    }

    // 그룹 챌린지 생성
    @PostMapping("/groups/{groupId}/create/{userId}")
    public ApiResponse<ChallengeCreateResponse> createGroupChallenge(
            @PathVariable Long groupId,
            @PathVariable Long userId,
            @Valid @RequestBody ChallengeCreateRequest request) {

        log.info("그룹 챌린지 생성 요청: {}", request);
        ChallengeCreateResponse response = challengeService.createGroupChallenge(request, groupId, userId);
        return new ApiResponse<>(response);
    }

    // 사용자의 진행중인 챌린지 조회
    @GetMapping("/ongoing/{userId}")
    public ApiResponse<List<UserOngoingChallengeResponse>> getUserOngoingChallenges(
            @PathVariable("userId") Long userId) {

        return new ApiResponse<>(challengeService.getUserOngoingChallenges(userId));
    }

    // 공개 챌린지 조회 (필터링 + 정렬)
    @GetMapping("/public")
    public ApiResponse<List<PublicChallengeResponse>> getPublicChallenges(
            @RequestParam(required = false) ChallengeSortType sortType) {

        return new ApiResponse<>(challengeService.getPublicChallenges(sortType));
    }

    // 친구 공개 챌린지 조회
    @GetMapping("/friends/{userId}")
    public ApiResponse<List<PublicChallengeResponse>> getFriendsChallenges(
            @PathVariable("userId") Long userId) {

        return new ApiResponse<>(challengeService.getFriendsChallenges(userId));
    }

    // 챌린지 참가
    @PostMapping("/{challengeId}/join/{userId}")
    public ApiResponse<Void> joinChallenge(
            @PathVariable Long challengeId,
            @PathVariable("userId") Long userId) {

        challengeService.joinChallenge(challengeId, userId);
        return new ApiResponse<>(null);
    }

    // 챌린지 나가기
    @PostMapping("/{challengeId}/leave/{userId}")
    public ApiResponse<Void> leaveChallenge(
            @PathVariable Long challengeId,
            @PathVariable("userId") Long userId) {

        challengeService.leaveChallenge(challengeId, userId);
        return new ApiResponse<>(null);
    }

    // 유저의 완료된 챌린지 리턴
    @GetMapping("/completed/unclaimed/{userId}")
    public ApiResponse<List<CompletedChallengeResponse>> getUnclaimedCompletedChallenges(
            @PathVariable("userId") Long userId) {

        return new ApiResponse<>(challengeService.getUnclaimedCompletedChallenges(userId));
    }

    // 유저의 보상 수령
    @PostMapping("/completed/claim/{challengeId}/{userId}")
    public ApiResponse<String> claimChallengeReward(
            @PathVariable("challengeId") Long challengeId,
            @PathVariable("userId") Long userId) {

        challengeService.claimChallengeReward(challengeId, userId);
        return new ApiResponse<>("보상이 지급되었습니다.");
    }
}