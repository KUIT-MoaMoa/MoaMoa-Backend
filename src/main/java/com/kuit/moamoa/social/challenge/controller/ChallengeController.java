package com.kuit.moamoa.social.challenge.controller;

import com.kuit.moamoa.social.challenge.domain.ChallengeCategory;
import com.kuit.moamoa.social.challenge.domain.ChallengeSortType;
import com.kuit.moamoa.social.challenge.dto.request.ChallengeCreateRequest;
import com.kuit.moamoa.configuration.response.ApiResponse;
import com.kuit.moamoa.global.jwt.Jwt;
import com.kuit.moamoa.social.challenge.service.ChallengeService;
import com.kuit.moamoa.social.challenge.dto.response.ChallengeCreateResponse;
import com.kuit.moamoa.social.challenge.dto.response.ChallengeMemberProgressResponse;
import com.kuit.moamoa.social.challenge.dto.response.CompletedChallengeResponse;
import com.kuit.moamoa.social.challenge.dto.response.PublicChallengeResponse;
import com.kuit.moamoa.social.challenge.dto.response.UserOngoingChallengeResponse;
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

        List<UserOngoingChallengeResponse> responses = challengeService.getUserOngoingChallenges(userId);
        return new ApiResponse<>(responses);
    }

    // 공개 챌린지 조회 (필터링 + 정렬)
    @GetMapping("/public")
    public ApiResponse<List<PublicChallengeResponse>> getPublicChallenges(
            @RequestParam(required = false) ChallengeSortType sortType,
            @Jwt Long userId) {

        List<PublicChallengeResponse> challenges = challengeService.getPublicChallenges(sortType, userId);
        return new ApiResponse<>(challenges);
    }

    // 친구 공개 챌린지 조회
    @GetMapping("/friends")
    public ApiResponse<List<PublicChallengeResponse>> getFriendsChallenges(
            @Jwt Long userId) {

        List<PublicChallengeResponse> friends = challengeService.getFriendsChallenges(userId);
        return new ApiResponse<>(friends);
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
    @DeleteMapping("/{challengeId}/leave")
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

        List<CompletedChallengeResponse> completedChallenges = challengeService.getUnclaimedCompletedChallenges(userId);
        return new ApiResponse<>(completedChallenges);
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

    // 카테고리로 챌린지 검색
    @GetMapping("/search/category")
    public ApiResponse<List<PublicChallengeResponse>> searchByCategory(
            @RequestParam ChallengeCategory category,
            @Jwt Long userId) {

        List<PublicChallengeResponse> responses = challengeService.searchChallengesByCategory(category, userId);
        return new ApiResponse<>(responses);
    }

    // 검색어로 챌린지 검색
    @GetMapping("/search/keyword")
    public ApiResponse<List<PublicChallengeResponse>> searchByKeyword(
            @RequestParam String keyword,
            @Jwt Long userId) {

        List<PublicChallengeResponse> responses = challengeService.searchChallengesByKeyword(keyword, userId);
        return new ApiResponse<>(responses);
    }
}