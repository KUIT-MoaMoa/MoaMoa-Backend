package com.kuit.moamoa.global.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HomeResponse {
    private String nickName;
    private boolean needOverConsumptionTest;
    private ConsumptionChallengeSummary consumptionChallengeSummary;
    private CoinSummary coinSummary;
    private ChallengeHomeResponse.ChallengeHomeSummaryResponse challengeHomeResponse;

    public HomeResponse(String nickName, boolean needOverConsumptionTest, ConsumptionChallengeSummary consumptionChallengeSummary, int coin, ChallengeHomeResponse.ChallengeHomeSummaryResponse challengeHomeResponse) { // ✅ 변경됨
        this.nickName = nickName;
        this.needOverConsumptionTest = needOverConsumptionTest;
        this.consumptionChallengeSummary = consumptionChallengeSummary;
        this.coinSummary = new CoinSummary(coin);
        this.challengeHomeResponse = challengeHomeResponse;
    }
}
