package com.kuit.moamoa.global.home.service;

import com.kuit.moamoa.attendance.repository.AttendanceRepository;
import com.kuit.moamoa.social.challenge.domain.Challenge;
import com.kuit.moamoa.social.challenge.domain.ChallengeProgress;
import com.kuit.moamoa.user.domain.Attendance;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.global.home.dto.ChallengeHomeResponse;
import com.kuit.moamoa.global.home.dto.ConsumptionChallengeSummary;
import com.kuit.moamoa.global.home.dto.HomeResponse;
import com.kuit.moamoa.configuration.exception.ErrorCode;
import com.kuit.moamoa.configuration.exception.GlobalException;
import com.kuit.moamoa.social.challenge.repository.ChallengeRepository;
import com.kuit.moamoa.consumption.challenge.repository.ConsumptionChallengeRepository;
import com.kuit.moamoa.user.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
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
    private final AttendanceRepository attendanceRepository;

    public HomeResponse getOverallSummary(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);

        LocalDate today = LocalDate.now();

        // 이번 주 월요일과 일요일 찾기
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // LocalDateTime 변환
        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(23, 59, 59);

        // 출석 기록 조회
        List<Attendance> attendances = attendanceRepository.findAttendancesForCurrentWeek(startDateTime, endDateTime);

        List<String> attendanceDates = attendances.stream()
                .map(attendance -> attendance.getCreatedAt().toLocalDate().format(DateTimeFormatter.ISO_DATE)) // "yyyy-MM-dd"
                .distinct()
                .toList();

        ConsumptionChallengeSummary consumptionChallengeSummary = consumptionChallengeRepository
                .findByUserAndEndDateGreaterThanEqual(user, LocalDate.now())
                .map(ConsumptionChallengeSummary::of)
                .orElse(ConsumptionChallengeSummary.empty());

        ChallengeHomeResponse.ChallengeHomeSummaryResponse challengeHomeSummary = getChallengeHomeSummary(user);

        return new HomeResponse(    // HomeResponse에 필요한 값들을 수정하고 constructor도 수정하자!
                user.getNickname(),
                user.isNeedOverConsumptionTest(),
                consumptionChallengeSummary,
                user.getCoin(),
                challengeHomeSummary,
                attendanceDates
        );
    }

    @Transactional
    public ChallengeHomeResponse.ChallengeHomeSummaryResponse getChallengeHomeSummary(User user) {
        Long userId = user.getId();

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
                                        .content(challenge.getContent())
                                        .publicChallenge(challenge.getPublicChallenge())
                                        .startDate(challenge.getStartDate())
                                        .endDate(challenge.getEndDate())
                                        .duration(ChronoUnit.DAYS.between(challenge.getStartDate(), challenge.getEndDate()))
                                        .battleCoin(challenge.getBattleCoin())
                                        .participantCount(challenge.getProgressList().size())
                                        .status(challenge.getStatus())
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
