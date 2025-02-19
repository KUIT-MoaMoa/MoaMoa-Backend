package com.kuit.moamoa.global.home.service;

import com.kuit.moamoa.social.challenge.domain.Challenge;
import com.kuit.moamoa.social.challenge.domain.ChallengeProgress;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.global.home.dto.ChallengeHomeResponse;
import com.kuit.moamoa.global.home.dto.ConsumptionChallengeSummary;
import com.kuit.moamoa.global.home.dto.HomeResponse;
import com.kuit.moamoa.configuration.exception.ErrorCode;
import com.kuit.moamoa.configuration.exception.GlobalException;
import com.kuit.moamoa.social.challenge.repository.ChallengeRepository;
import com.kuit.moamoa.consumption.challenge.repository.ConsumptionChallengeRepository;
import com.kuit.moamoa.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final UserRepository userRepository;
    private final ConsumptionChallengeRepository consumptionChallengeRepository;
    private final ChallengeRepository challengeRepository;

    public HomeResponse getOverallSummary(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);

        ConsumptionChallengeSummary consumptionChallengeSummary = consumptionChallengeRepository
                .findByUserAndEndDateGreaterThanEqual(user, LocalDate.now())
                .map(ConsumptionChallengeSummary::of)
                .orElse(ConsumptionChallengeSummary.empty());

        ChallengeHomeResponse.ChallengeHomeSummaryResponse challengeHomeSummary = getChallengeHomeSummary(userId);

        return new HomeResponse(    // HomeResponse에 필요한 값들을 수정하고 constructor도 수정하자!
                user.isNeedOverConsumptionTest(),
                consumptionChallengeSummary,
                user.getCoin(),
                challengeHomeSummary
        );
    }

    @Transactional
    public ChallengeHomeResponse.ChallengeHomeSummaryResponse getChallengeHomeSummary(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND, "User not found"));

        List<Challenge> participatingChallenges = challengeRepository.findAllOngoingChallengesByUserId(userId);

        if (!participatingChallenges.isEmpty()) {
            List<ChallengeHomeResponse.ParticipatingChallengeResponse> participatingResponses =
                    participatingChallenges.stream()
                            .map(challenge -> {
                                ChallengeProgress progress = challenge.getProgressList().stream()
                                        .filter(p -> p.getUser().getId().equals(userId))
                                        .findFirst()
                                        .orElseThrow(() -> new GlobalException(
                                                ErrorCode.NOT_PARTICIPATING,
                                                "User should be participating in this challenge"
                                        ));

                                return ChallengeHomeResponse.ParticipatingChallengeResponse.builder()
                                        .challengeId(challenge.getId())
                                        .title(challenge.getTitle())
                                        .usageRate(progress.getUsagePercentage())
                                        .build();
                            })
                            .collect(Collectors.toList());

            return ChallengeHomeResponse.ChallengeHomeSummaryResponse.builder()
                    .hasParticipatingChallenges(true)
                    .participatingChallenges(participatingResponses)
                    .recruitingChallenges(null)
                    .build();
        } else {
            List<Challenge> recruitingChallenges = challengeRepository.findChallengesByCreatedAtDesc(userId);

            List<ChallengeHomeResponse.RecruitingChallengeResponse> recruitingResponses =
                    recruitingChallenges.stream()
                            .map(ChallengeHomeResponse.RecruitingChallengeResponse::from)
                            .collect(Collectors.toList());

            return ChallengeHomeResponse.ChallengeHomeSummaryResponse.builder()
                    .hasParticipatingChallenges(false)
                    .participatingChallenges(null)
                    .recruitingChallenges(recruitingResponses)
                    .build();
        }
    }

    public void checkOverConsumptionTest(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        user.setNeedOverConsumptionTest(false);
        userRepository.save(user);
    }


}
