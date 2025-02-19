package com.kuit.moamoa.global.home.dto;

import com.kuit.moamoa.consumption.challenge.domain.ConsumptionChallenge;
import lombok.Getter;

@Getter
public class ConsumptionChallengeSummary {
    Integer consumptionLeft;
    Integer totalConsumption;
    Integer consumptionPercentile;

    private ConsumptionChallengeSummary(Integer consumptionLeft, Integer totalConsumption,
                                       Integer consumptionPercentile) {
        this.consumptionLeft = consumptionLeft;
        this.totalConsumption = totalConsumption;
        this.consumptionPercentile = consumptionPercentile;
    }

    public static ConsumptionChallengeSummary of(ConsumptionChallenge consumptionChallenge) {
        int targetAmount = consumptionChallenge.getTargetAmount();
        int totalSpent = consumptionChallenge.getConsumptions().stream()
                .mapToInt(consumption -> Math.toIntExact(consumption.getAmount()))
                .sum();

        return new ConsumptionChallengeSummary(
                targetAmount - totalSpent,
                totalSpent,
                (int) ((totalSpent / (double) targetAmount) * 100) // 올바른 백분율 계산
        );

    }

    public static ConsumptionChallengeSummary empty() {
        return new ConsumptionChallengeSummary(null, null, null);
    }
}
