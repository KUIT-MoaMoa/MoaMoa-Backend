package com.kuit.moamoa.consumption.challenge.service;

import com.kuit.moamoa.social.challenge.domain.ChallengeProgress;
import com.kuit.moamoa.social.challenge.domain.ChallengeStatus;
import com.kuit.moamoa.consumption.record.domain.Consumption;
import com.kuit.moamoa.consumption.challenge.domain.ConsumptionChallenge;
import com.kuit.moamoa.consumption.challenge.domain.ConsumptionChallengeSortType;
import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.consumption.record.dto.AddMyConsumptionRequest;
import com.kuit.moamoa.consumption.challenge.dto.RecentConsumptionChallengeGoalResponse;
import com.kuit.moamoa.consumption.challenge.dto.ConsumptionChallengeResponse;
import com.kuit.moamoa.consumption.challenge.dto.ConsumptionChallengeSummaryResponse;
import com.kuit.moamoa.consumption.challenge.dto.CreateConsumptionChallengeRequest;
import com.kuit.moamoa.consumption.record.dto.AddMyConsumptionResponse;
import com.kuit.moamoa.social.challenge.repository.ChallengeProgressRepository;
import com.kuit.moamoa.consumption.challenge.repository.ConsumptionChallengeRepository;
import com.kuit.moamoa.consumption.record.repository.ConsumptionRepository;
import com.kuit.moamoa.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumptionChallengeService {
    private final ConsumptionChallengeRepository consumptionChallengeRepository;
    private final ConsumptionRepository consumptionRepository;
    private final UserRepository userRepository;
    private final ChallengeProgressRepository challengeProgressRepository;
    public ConsumptionChallengeResponse createConsumptionChallenge(Long userId,
                                                                   CreateConsumptionChallengeRequest createConsumptionChallengeRequest)
            throws Exception {
        ConsumptionChallenge newConsumptionChallenge = ConsumptionChallenge.builder()
                .startDate(createConsumptionChallengeRequest.getStartDate())
                .endDate(createConsumptionChallengeRequest.getEndDate())
                .targetAmount(createConsumptionChallengeRequest.getTargetAmount())
                .build();

        userRepository.findById(userId).orElseThrow(Exception::new).addConsumptionChallenge(newConsumptionChallenge);
        consumptionChallengeRepository.save(newConsumptionChallenge);

        return new ConsumptionChallengeResponse(
                newConsumptionChallenge.getPrize(),
                newConsumptionChallenge.getStartDate(),
                newConsumptionChallenge.getEndDate(),
                newConsumptionChallenge.getTargetAmount()
        );
    }

    public List<ConsumptionChallengeSummaryResponse> lookUpConsumptionChallengeSummary(Long userId, ConsumptionChallengeSortType sortType) throws Exception {   // TODO: 검색 옵션
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        List<ConsumptionChallenge> consumptionChallenge = null;

        if(Objects.equals(sortType.toString(), "LATEST")) {
            consumptionChallenge = consumptionChallengeRepository.findAllByUserAndEndDateBeforeOrderByStartDateDesc(user, LocalDate.now());   // TODO: null은 던지지 말자
        }

        if(Objects.equals(sortType.toString(), "EARLIEST")) {
            consumptionChallenge = consumptionChallengeRepository.findAllByUserAndEndDateBeforeOrderByStartDateAsc(user, LocalDate.now());   // TODO: null은 던지지 말자
        }
        if(Objects.equals(sortType.toString(), "COIN")) {

            consumptionChallenge = consumptionChallengeRepository.findAllByUserAndEndDateBeforeOrderByPrizeDesc(user, LocalDate.now());   // TODO: null은 던지지 말자
        }

        if (consumptionChallenge == null) {
            return null;
        }

        return consumptionChallenge.stream()
                .map(ConsumptionChallengeSummaryResponse::new)
                .collect(Collectors.toList());
    }

    public RecentConsumptionChallengeGoalResponse lookUpRecentTargetGoal(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        ConsumptionChallenge consumptionChallenge = consumptionChallengeRepository.findFirstByUserOrderByStartDateDesc(
                        user)
                .orElse(null);
        if(consumptionChallenge == null) {
            return new RecentConsumptionChallengeGoalResponse(0);
        }
        return new RecentConsumptionChallengeGoalResponse(consumptionChallenge.getTargetAmount());
    }

    public AddMyConsumptionResponse getCurrentAmountLeft(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        ConsumptionChallenge consumptionChallenge = consumptionChallengeRepository.findFirstByUserOrderByStartDateDesc(
                user).get();
        int totalSpent = consumptionChallenge.getConsumptions().stream()
                .mapToInt(consumption -> Math.toIntExact(consumption.getAmount()))
                .sum();

        return new AddMyConsumptionResponse(consumptionChallenge.getTargetAmount() - totalSpent);
    }

    public Object addMyConsumption(Long userId, AddMyConsumptionRequest addMyConsumptionRequest) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        ConsumptionChallenge consumptionChallenge = consumptionChallengeRepository.findFirstByUserOrderByStartDateDesc(
                user).get();
        log.warn("{} {}", addMyConsumptionRequest.getConsumptionCategory(),
                addMyConsumptionRequest.getChallengeCategory());
        Consumption consumption = Consumption.builder()
                .user(user)
                .amount((long) addMyConsumptionRequest.getAmount())
                .consumptionCategory(addMyConsumptionRequest.getConsumptionCategory())
                .consumptionChallenge(consumptionChallenge)
                .challengeCategory(addMyConsumptionRequest.getChallengeCategory())
                .status(Status.ACTIVE)
                .build();
        consumptionRepository.save(consumption);
        consumption.setConsumptionChallenge(consumptionChallenge);
        consumption.setUser(user);

        // TODO: Challenge Progress도 수정 해줘야함
        List<ChallengeProgress> challengeProgress = challengeProgressRepository.findAllByUser(user);

        List<ChallengeProgress> refinedProgress = challengeProgress.stream()
                .filter(x -> x.getChallenge().getChallengeCategory()
                        .equals(addMyConsumptionRequest.getChallengeCategory()))
                .filter(x -> x.getChallenge().getStatus().equals(ChallengeStatus.ONGOING))
                .collect(Collectors.toList());

        refinedProgress
                .forEach(x -> {
                    x.updateUsedAmount(addMyConsumptionRequest.getAmount());
                    challengeProgressRepository.save(x);
                });
        return null;
    }
}
