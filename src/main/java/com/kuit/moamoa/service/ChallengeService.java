package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.*;
import com.kuit.moamoa.dto.request.challenge.ChallengeCreateRequest;
import com.kuit.moamoa.dto.response.challenge.ChallengeCreateResponse;
import com.kuit.moamoa.dto.response.challenge.CompletedChallengeResponse;
import com.kuit.moamoa.dto.response.challenge.PublicChallengeResponse;
import com.kuit.moamoa.dto.response.challenge.UserOngoingChallengeResponse;
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

    // 그룹 챌린지 생성
    @Transactional
    public ChallengeCreateResponse createGroupChallenge(ChallengeCreateRequest request,
                                                        Long groupId, Long userId) {
        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new GlobalException(ErrorCode.USER_GROUP_NOT_FOUND, "User group not found"));

        User user = findUserById(userId);
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
                        .title(challenge.getTitle())
                        .startDate(challenge.getStartDate())
                        .endDate(challenge.getEndDate())
                        .duration(challenge.getDuration())
                        .participantCount(challenge.getProgressList().size())
                        .build())
                .collect(Collectors.toList());
    }


    //공개 챌린지 조회(정렬)
    public List<PublicChallengeResponse> getPublicChallenges(ChallengeSortType sortType) {
        if (sortType == null) {
            sortType = ChallengeSortType.POPULARITY; // 기본 정렬을 인기순으로 설정
        }
        
        List<Challenge> challenges = switch (sortType) {
            case LATEST -> challengeRepository.findPublicChallengesByCreatedAtDesc();
            case DEADLINE -> challengeRepository.findPublicChallengesByRecruitmentDeadlineAsc();
            case COIN -> challengeRepository.findPublicChallengesByBattleCoinDesc();

            //기본은 인기순
            default -> challengeRepository.findPublicChallengesByParticipantCountDesc();
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

    // 특정 사용자가 해당 챌린지에서 보상을 받았는지 확인하는 메서드 추가
    private boolean isRewardClaimed(Challenge challenge, Long userId) {
        return challenge.getProgressList().stream()
                .filter(progress -> progress.getUser().getId().equals(userId))
                .findFirst()
                .map(ChallengeProgress::isRewardClaimed)
                .orElse(false);
    }

    // 보상 지급 메서드
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

    private void refundBattleCoins(Challenge challenge) {
        for (ChallengeProgress progress : challenge.getProgressList()) {
            userService.addBattleCoins(
                    progress.getUser().getId(),
                    challenge.getBattleCoin()
            );
        }
    }

    // 챌린지 완료 처리
    public void completeChallenge(Long challengeId) {
        Challenge challenge = findChallengeById(challengeId);
        challenge.completeChallenge();

        challengeRepository.save(challenge);
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