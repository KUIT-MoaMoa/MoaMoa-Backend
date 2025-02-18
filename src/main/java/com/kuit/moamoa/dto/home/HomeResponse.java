package com.kuit.moamoa.dto.home;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HomeResponse {
    private boolean needOverConsumptionTest;
    private ConsumptionChallengeSummary consumptionChallengeSummary;
    private CoinSummary coinSummary;

    public HomeResponse(boolean needOverConsumptionTest, ConsumptionChallengeSummary consumptionChallengeSummary, int coin) {
        this.needOverConsumptionTest = needOverConsumptionTest;
        this.consumptionChallengeSummary = consumptionChallengeSummary;
        this.coinSummary = new CoinSummary(coin);
    }
}
