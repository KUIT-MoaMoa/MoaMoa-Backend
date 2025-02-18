package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.ChallengeProgress;
import com.kuit.moamoa.domain.ConsumptionChallenge;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.CoinRecordResponse;
import com.kuit.moamoa.dto.CoinRecordResponse.Transaction;
import com.kuit.moamoa.repository.ChallengeProgressRepository;
import com.kuit.moamoa.repository.ConsumptionChallengeRepository;
import com.kuit.moamoa.repository.UserRepository;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
