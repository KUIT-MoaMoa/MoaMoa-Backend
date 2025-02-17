package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.ConsumptionChallenge;
import com.kuit.moamoa.domain.ConsumptionChallengeSortType;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.RecentConsumptionChallengeGoalResponse;
import com.kuit.moamoa.dto.ConsumptionChallengeResponse;
import com.kuit.moamoa.dto.ConsumptionChallengeSummaryResponse;
import com.kuit.moamoa.dto.CreateConsumptionChallengeRequest;
import com.kuit.moamoa.repository.ConsumptionChallengeRepository;
import com.kuit.moamoa.repository.ConsumptionRepository;
import com.kuit.moamoa.repository.UserRepository;
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
            consumptionChallenge = consumptionChallengeRepository.findAllByUserOrderByStartDateDesc(user);   // TODO: null은 던지지 말자
        }

        if(Objects.equals(sortType.toString(), "EARLIEST")) {
            consumptionChallenge = consumptionChallengeRepository.findAllByUserOrderByStartDateAsc(user);   // TODO: null은 던지지 말자
        }
        if(Objects.equals(sortType.toString(), "COIN")) {

            consumptionChallenge = consumptionChallengeRepository.findAllByUserOrderByPrizeDesc(user);   // TODO: null은 던지지 말자
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
}
