package com.kuit.moamoa.service;

import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.dto.home.ConsumptionChallengeSummary;
import com.kuit.moamoa.dto.home.HomeResponse;
import com.kuit.moamoa.repository.ConsumptionChallengeRepository;
import com.kuit.moamoa.repository.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeService {
    private final UserRepository userRepository;
    private final ConsumptionChallengeRepository consumptionChallengeRepository;

    public HomeResponse getOverallSummary(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);

        ConsumptionChallengeSummary consumptionChallengeSummary = consumptionChallengeRepository
                .findByUserAndEndDateGreaterThanEqual(user, LocalDate.now())
                .map(ConsumptionChallengeSummary::of)
                .orElse(ConsumptionChallengeSummary.empty());

        return new HomeResponse(    // HomeResponse에 필요한 값들을 수정하고 constructor도 수정하자!
                user.isNeedOverConsumptionTest(),
                consumptionChallengeSummary,
                user.getCoin()
        );
    }

    public void checkOverConsumptionTest(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(Exception::new);
        user.setNeedOverConsumptionTest(false);
        userRepository.save(user);
    }
}
