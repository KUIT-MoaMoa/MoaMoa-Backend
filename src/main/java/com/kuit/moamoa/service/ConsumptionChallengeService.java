package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.ConsumptionChallenge;
import com.kuit.moamoa.dto.ConsumptionChallengeResponse;
import com.kuit.moamoa.dto.CreateConsumptionChallengeRequest;
import com.kuit.moamoa.repository.ConsumptionChallengeRepository;
import com.kuit.moamoa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumptionChallengeService {
    private final ConsumptionChallengeRepository consumptionChallengeRepository;
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
                calculatePrize(),
                newConsumptionChallenge.getStartDate(),
                newConsumptionChallenge.getEndDate(),
                newConsumptionChallenge.getTargetAmount()
        );
    }

    private int calculatePrize() {  // THIS IS A MOCK
        return 20;
    }
}
