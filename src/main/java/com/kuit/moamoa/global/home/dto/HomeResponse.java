package com.kuit.moamoa.global.home.dto;

import com.kuit.moamoa.user.domain.Attendance;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class HomeResponse {
    private String nickName;
    private boolean needOverConsumptionTest;
    private ConsumptionChallengeSummary consumptionChallengeSummary;
    private CoinSummary coinSummary;
    private ChallengeHomeResponse.ChallengeHomeSummaryResponse challengeHomeResponse;
    private List<String> attendanceDates;

    public HomeResponse(String nickName, boolean needOverConsumptionTest, ConsumptionChallengeSummary consumptionChallengeSummary,
                        int coin, ChallengeHomeResponse.ChallengeHomeSummaryResponse challengeHomeResponse, List<String> attendanceDates) {
        this.nickName = nickName;
        this.needOverConsumptionTest = needOverConsumptionTest;
        this.consumptionChallengeSummary = consumptionChallengeSummary;
        this.coinSummary = new CoinSummary(coin);
        this.challengeHomeResponse = challengeHomeResponse;
        this.attendanceDates = attendanceDates;
    }
}
