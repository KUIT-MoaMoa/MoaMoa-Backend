package com.kuit.moamoa.controller;

import com.kuit.moamoa.domain.ChallengeSortType;
import com.kuit.moamoa.dto.request.challenge.ChallengeCreateRequest;
import com.kuit.moamoa.dto.response.challenge.*;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.ChallengeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/challenges")
public class ChallengeController {
    private final ChallengeService challengeService;

    // 일반 챌린지 생성
    @PostMapping("/create")
    public ApiResponse<ChallengeCreateResponse> createChallenge(
            @Jwt Long userId,
            @Valid @RequestBody ChallengeCreateRequest request) {

        log.info("일반 챌린지 생성 요청: {}", request);
        ChallengeCreateResponse response = challengeService.createChallenge(request, userId);
        return new ApiResponse<>(response);
    }

    // 그룹 챌린지 생성
    @PostMapping("/groups/{groupId}/create")
    public ApiResponse<ChallengeCreateResponse> createGroupChallenge(
            @PathVariable Long groupId,
            @Jwt Long userId,
            @Valid @RequestBody ChallengeCreateRequest request) {

        log.info("그룹 챌린지 생성 요청: {}", request);
        ChallengeCreateResponse response = challengeService.createGroupChallenge(request, groupId, userId);
        return new ApiResponse<>(response);
    }

    // 사용자의 진행중인 챌린지 조회
    @GetMapping("/ongoing")
    public ApiResponse<List<UserOngoingChallengeResponse>> getUserOngoingChallenges(
            @Jwt Long userId) {

        return new ApiResponse<>(challengeService.getUserOngoingChallenges(userId));
    }

    // 공개 챌린지 조회 (필터링 + 정렬)
    @GetMapping("/public")
    public ApiResponse<List<PublicChallengeResponse>> getPublicChallenges(
            @RequestParam(required = false) ChallengeSortType sortType,
            @Jwt Long userId) {

        return new ApiResponse<>(challengeService.getPublicChallenges(sortType, userId));
    }

    // 친구 공개 챌린지 조회
    @GetMapping("/friends")
    public ApiResponse<List<PublicChallengeResponse>> getFriendsChallenges(
            @Jwt Long userId) {

        return new ApiResponse<>(challengeService.getFriendsChallenges(userId));
    }

    // 챌린지 참가
    @PostMapping("/{challengeId}/join")
    public ApiResponse<String> joinChallenge(
            @PathVariable Long challengeId,
            @Jwt Long userId) {

        challengeService.joinChallenge(challengeId, userId);
        return new ApiResponse<>("챌린지에 성공적으로 참여했습니다.");
    }

    // 챌린지 나가기
    @PostMapping("/{challengeId}/leave")
    public ApiResponse<String> leaveChallenge(
            @PathVariable Long challengeId,
            @Jwt Long userId) {

        challengeService.leaveChallenge(challengeId, userId);
        return new ApiResponse<>("챌린지를 성공적으로 나갔습니다.");
    }

    // 유저의 완료된 챌린지 리턴
    @GetMapping("/completed/unclaimed")
    public ApiResponse<List<CompletedChallengeResponse>> getUnclaimedCompletedChallenges(
            @Jwt Long userId) {

        return new ApiResponse<>(challengeService.getUnclaimedCompletedChallenges(userId));
    }

    // 유저의 보상 수령
    @PostMapping("/completed/claim/{challengeId}")
    public ApiResponse<String> claimChallengeReward(
            @PathVariable Long challengeId,
            @Jwt Long userId) {

        challengeService.claimChallengeReward(challengeId, userId);
        return new ApiResponse<>("보상이 지급되었습니다.");
    }

    // 함께하는 챌린저
    @GetMapping("/{challengeId}/members/progress")
    public ApiResponse<ChallengeMemberProgressResponse> getChallengeMemberProgress(
            @PathVariable Long challengeId,
            @Jwt Long userId) {
        return new ApiResponse<>(challengeService.getChallengeMemberProgress(challengeId, userId));
    }
}