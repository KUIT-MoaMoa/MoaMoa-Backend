package com.kuit.moamoa.dto.home;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HomeResponse {
    private boolean needOverConsumptionTest;
    private ConsumptionChallengeSummary consumptionChallengeSummary;
    private CoinSummary coinSummary;
    private ChallengeHomeResponse.ChallengeHomeSummaryResponse challengeHomeResponse;

    public HomeResponse(boolean needOverConsumptionTest, ConsumptionChallengeSummary consumptionChallengeSummary, int coin, ChallengeHomeResponse.ChallengeHomeSummaryResponse challengeHomeResponse) { // ✅ 변경됨
        this.needOverConsumptionTest = needOverConsumptionTest;
        this.consumptionChallengeSummary = consumptionChallengeSummary;
        this.coinSummary = new CoinSummary(coin);
        this.challengeHomeResponse = challengeHomeResponse;
    }
}
