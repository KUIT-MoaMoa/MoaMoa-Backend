package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.*;
import com.kuit.moamoa.dto.request.challenge.ChallengeCreateRequest;
import com.kuit.moamoa.dto.response.challenge.*;
import com.kuit.moamoa.global.exception.GlobalException;
import com.kuit.moamoa.global.exception.ErrorCode;
import com.kuit.moamoa.repository.ChallengeRepository;
import com.kuit.moamoa.repository.UserGroupRepository;
import com.kuit.moamoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChallengeService { // TODO: UserService에 코인 추가 제거 서비스 구현
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserService userService;

    // 일반 챌린지 생성
    @Transactional
    public ChallengeCreateResponse createChallenge(ChallengeCreateRequest request, Long userId) {
        User user = findUserById(userId);

        validateCreateRequest(request, user);

        // 사용자의 배틀코인 차감
        userService.deductBattleCoins(userId, request.getBattleCoin());

        Challenge challenge = Challenge.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .headCount(request.getHeadCount())
                .duration(request.getDuration())
                .publicChallenge(request.getPublicChallenge())
                .goalAmount(request.getGoalAmount())
                .battleCoin(request.getBattleCoin())
                .challengeCategory(request.getChallengeCategory())
                .startDate(request.getStartDate())
                .build();

        // 진행 상태 추가
        ChallengeProgress progress = new ChallengeProgress(challenge, user, Status.ACTIVE);
        challenge.getProgressList().add(progress);

        Challenge savedChallenge = challengeRepository.save(challenge);
        return new ChallengeCreateResponse(savedChallenge.getId(), "일반 챌린지 생성 성공");
    }

    private void validateCreateRequest(ChallengeCreateRequest request, User user) {
        if (request.getBattleCoin() > user.getBattleCoins()) {
            throw new GlobalException(ErrorCode.INSUFFICIENT_COINS, "Not enough battle coins");
        }

        if (request.getStartDate().isBefore(LocalDateTime.now())) {
            throw new GlobalException(ErrorCode.INVALID_DATE, "Start date cannot be in the past");
        }
    }

    // 그룹 챌린지 생성
    @Transactional
    public ChallengeCreateResponse createGroupChallenge(ChallengeCreateRequest request,
                                                        Long groupId, Long userId) {
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_GROUP_NOT_FOUND, "User group not found"));

        User user = findUserById(userId);

        validateCreateRequest(request, user);

        userService.deductBattleCoins(userId, request.getBattleCoin());

        Challenge challenge = Challenge.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .headCount(request.getHeadCount())
                .duration(request.getDuration())
                .publicChallenge(request.getPublicChallenge())
                .goalAmount(request.getGoalAmount())
                .battleCoin(request.getBattleCoin())
                .challengeCategory(request.getChallengeCategory())
                .startDate(request.getStartDate())
                .userGroup(group)  // 그룹 설정
                .build();

        // 그룹에 챌린지를 추가
        group.addChallenge(challenge);

        ChallengeProgress progress = new ChallengeProgress(challenge, user, Status.ACTIVE);
        challenge.getProgressList().add(progress);

        Challenge savedChallenge = challengeRepository.save(challenge);
        return new ChallengeCreateResponse(savedChallenge.getId(), "그룹 챌린지 생성 성공");
    }
    
    //사용자가 참여중인 챌린지 조회
    public List<UserOngoingChallengeResponse> getUserOngoingChallenges(Long userId) {
        findUserById(userId);

        return challengeRepository.findOngoingChallengesByUserId(userId).stream()
                .map(challenge -> UserOngoingChallengeResponse.builder()
                        .challengeId(challenge.getId())
                        .title(challenge.getTitle())
                        .startDate(challenge.getStartDate())
                        .endDate(challenge.getEndDate())
                        .duration(challenge.getDuration())
                        .participantCount(challenge.getProgressList().size())
                        .build())
                .collect(Collectors.toList());
    }


    //공개 챌린지 조회(정렬)
    public List<PublicChallengeResponse> getPublicChallenges(ChallengeSortType sortType, Long userId) {
        if (sortType == null) {
            sortType = ChallengeSortType.POPULARITY; // 기본 정렬을 인기순으로 설정
        }

        List<Challenge> challenges = switch (sortType) {
            case LATEST -> challengeRepository.findChallengesByCreatedAtDesc(userId);
            case DEADLINE -> challengeRepository.findChallengesByRecruitmentDeadlineAsc(userId);
            case COIN -> challengeRepository.findChallengesByBattleCoinDesc(userId);
            
            //기본은 인기순
            default -> challengeRepository.findChallengesByParticipantCountDesc(userId);
        };

        return challenges.stream()
                .map(PublicChallengeResponse::new)
                .collect(Collectors.toList());
    }
    
    //친구 공개 챌린지 조회
    public List<PublicChallengeResponse> getFriendsChallenges(Long userId) {
        findUserById(userId);

        List<Challenge> challenges = challengeRepository.findFriendsChallenges(userId);

        return challenges.stream()
                .map(PublicChallengeResponse::new)
                .collect(Collectors.toList());
    }

    // 챌린지 참여
    @Transactional
    public void joinChallenge(Long challengeId, Long userId) {
        Challenge challenge = findChallengeById(challengeId);
        User user = findUserById(userId);

        // 사용자의 배틀코인 차감
        userService.deductBattleCoins(userId, challenge.getBattleCoin());

        challenge.addParticipant(user);
        challengeRepository.save(challenge);
    }

    // 챌린지 나가기
    public void leaveChallenge(Long challengeId, Long userId) {
        Challenge challenge = findChallengeById(challengeId);
        User user = findUserById(userId);

        challenge.removeParticipant(user);
        challengeRepository.save(challenge);
    }

    @Transactional
    public void startChallengesForDate(LocalDateTime now) {
        List<Challenge> challengesToStart = challengeRepository
                .findByStatusAndStartDateLessThanEqual(ChallengeStatus.RECRUITING, now);

        for (Challenge challenge : challengesToStart) {
            try {
                challenge.startChallenge();
                challengeRepository.save(challenge);
                log.info("Started challenge: {}", challenge.getId());
            } catch (Exception e) {
                log.error("Failed to start challenge {}: {}", challenge.getId(), e.getMessage());
                // 시작 조건 미달인 경우 참가자들에게 코인 환불
                refundBattleCoins(challenge);
                challenge.cancel();
                challengeRepository.save(challenge);
            }
        }
    }

    //자정 이후 챌린지를 완료로 처리
    @Transactional
    public void completeChallengesForDate(LocalDateTime now) {
        List<Challenge> challengesToComplete = challengeRepository
                .findByStatusAndEndDateLessThanEqual(ChallengeStatus.ONGOING, now);

        for (Challenge challenge : challengesToComplete) {
            try {
                completeChallenge(challenge.getId());
                log.info("Completed challenge: {}", challenge.getId());
            } catch (Exception e) {
                log.error("Failed to complete challenge {}: {}", challenge.getId(), e.getMessage());
            }
        }
    }

    // 유저의 완료된 챌린지 리턴
    @Transactional(readOnly = true)
    public List<CompletedChallengeResponse> getUnclaimedCompletedChallenges(Long userId) {
        findUserById(userId);

        return challengeRepository.findUnclaimedCompletedChallengesByUserId(userId).stream()
                .map(challenge -> CompletedChallengeResponse.builder()
                        .challengeId(challenge.getId())
                        .title(challenge.getTitle())
                        .battleCoin(challenge.getBattleCoin()) // int 타입 확인 필요
                        .endDate(challenge.getEndDate())
                        .status(challenge.getStatus())
                        .rewardClaimed(isRewardClaimed(challenge, userId)) // 개별 참여자의 보상 상태 확인
                        .build())
                .collect(Collectors.toList());
    }

    // 보상 지급 서비스
    @Transactional
    public void claimChallengeReward(Long challengeId, Long userId) {
        ChallengeProgress progress = challengeRepository
                .findProgressByChallengeIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new GlobalException(
                        ErrorCode.NOT_PARTICIPATING,
                        "User is not part of this challenge"
                ));

        if (!progress.isGoalAchieved() || progress.isRewardClaimed()) {
            throw new GlobalException(
                    ErrorCode.REWARD_ALREADY_CLAIMED,
                    "Reward already claimed or challenge not completed successfully"
            );
        }

        // 배틀코인 지급 (UserService 필요)
        userService.addBattleCoins(userId, progress.getChallenge().getBattleCoin() * 2);

        // 보상 상태 업데이트
        progress.claimReward();
        challengeRepository.save(progress.getChallenge());
    }

    // 함께하는 챌린저 조회
    @Transactional(readOnly = true)
    public ChallengeMemberProgressResponse getChallengeMemberProgress(Long challengeId, Long loginUserId) {
        Challenge challenge = findChallengeById(challengeId);

        // 로그인한 사용자의 진행률
        UserProgressResponse userProgress = challenge.getProgressList().stream()
                .filter(progress -> progress.getUser().getId().equals(loginUserId))
                .findFirst()
                .map(UserProgressResponse::from)
                .orElseThrow(() -> new GlobalException(
                        ErrorCode.NOT_PARTICIPATING,
                        "User is not participating in this challenge"
                ));

        // 다른 참여자들의 정보
        List<OtherMemberProgressResponse> otherMembersProgress = challenge.getProgressList().stream()
                .filter(progress -> !progress.getUser().getId().equals(loginUserId))
                .map(OtherMemberProgressResponse::from)
                .collect(Collectors.toList());

        return ChallengeMemberProgressResponse.builder()
                .userProgress(userProgress)
                .otherMembersProgress(otherMembersProgress)
                .build();
    }

    // 카테고리로 챌린지 검색
    @Transactional(readOnly = true)
    public List<PublicChallengeResponse> searchChallengesByCategory(ChallengeCategory category, Long userId) {
        findUserById(userId); // 사용자 존재 확인

        List<Challenge> challenges = challengeRepository.findPublicChallengesByCategory(category, userId);
        return challenges.stream()
                .map(PublicChallengeResponse::new)
                .collect(Collectors.toList());
    }

    // 키워드로 챌린지 검색
    @Transactional(readOnly = true)
    public List<PublicChallengeResponse> searchChallengesByKeyword(String keyword, Long userId) {
        findUserById(userId); // 사용자 존재 확인

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new GlobalException(ErrorCode.INVALID_INPUT, "Search keyword cannot be empty");
        }

        List<Challenge> challenges = challengeRepository.findPublicChallengesByKeyword(keyword, userId);
        return challenges.stream()
                .map(PublicChallengeResponse::new)
                .collect(Collectors.toList());
    }

    // private method
    private void refundBattleCoins(Challenge challenge) {
        for (ChallengeProgress progress : challenge.getProgressList()) {
            userService.addBattleCoins(
                    progress.getUser().getId(),
                    challenge.getBattleCoin()
            );
        }
    }

    // 특정 사용자가 해당 챌린지에서 보상을 받았는지 확인하는 메서드
    private boolean isRewardClaimed(Challenge challenge, Long userId) {
        return challenge.getProgressList().stream()
                .filter(progress -> progress.getUser().getId().equals(userId))
                .findFirst()
                .map(ChallengeProgress::isRewardClaimed)
                .orElse(false);
    }

    // 챌린지 완료 처리 (Ongoing 상태 + endDate가 now보다 이전인 챌린지)
    public void completeChallenge(Long challengeId) {
        Challenge challenge = findChallengeById(challengeId);

        // 각 참여자의 목표 달성 여부 설정
        for (ChallengeProgress progress : challenge.getProgressList()) {
            boolean isGoalAchieved = checkGoalAchievement(progress);
            progress.setGoalAchieved(isGoalAchieved);
        }

        // 챌린지 상태 COMPLETED로 변경
        challenge.completeChallenge();

        challengeRepository.save(challenge);
    }

    private boolean checkGoalAchievement(ChallengeProgress progress) {
        Challenge challenge = progress.getChallenge();
        // 목표량과 사용자의 실제 달성량을 비교
        return progress.getUsedAmount() <= challenge.getGoalAmount();
    }

    private Challenge findChallengeById(Long challengeId) {
        return challengeRepository.findById(challengeId)
                .orElseThrow(() -> new GlobalException(
                        ErrorCode.CHALLENGE_NOT_FOUND,
                        "Challenge not found with id: " + challengeId
                ));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new GlobalException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + userId
                ));
    }
}