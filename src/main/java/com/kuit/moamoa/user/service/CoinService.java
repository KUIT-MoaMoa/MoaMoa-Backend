package com.kuit.moamoa.user.service;

import com.kuit.moamoa.social.challenge.domain.ChallengeProgress;
import com.kuit.moamoa.consumption.challenge.domain.ConsumptionChallenge;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.user.dto.CoinRecordResponse;
import com.kuit.moamoa.social.challenge.repository.ChallengeProgressRepository;
import com.kuit.moamoa.consumption.challenge.repository.ConsumptionChallengeRepository;
import com.kuit.moamoa.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinService {
    private final UserRepository userRepository;
    private final ChallengeProgressRepository challengeProgressRepository;
    private final ConsumptionChallengeRepository consumptionChallengeRepository;


    public CoinRecordResponse getUserCoinRecord(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);

        List<ChallengeProgress> challengeProgresses = challengeProgressRepository.findAllByUser(user);
        List<ConsumptionChallenge> consumptionChallenges = consumptionChallengeRepository.findAllByUserAndEndDateLessThan(
                user, LocalDate.now());

        return new CoinRecordResponse(user, challengeProgresses, consumptionChallenges);
    }





}
