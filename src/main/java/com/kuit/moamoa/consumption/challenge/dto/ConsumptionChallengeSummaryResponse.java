package com.kuit.moamoa.consumption.challenge.dto;

import com.kuit.moamoa.consumption.challenge.domain.ConsumptionChallenge;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import lombok.Getter;

@Getter
public class ConsumptionChallengeSummaryResponse {
    private boolean succeed;
    private int totalSpent;
    private int targetAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String title;
    private int prize;

    public ConsumptionChallengeSummaryResponse(ConsumptionChallenge consumptionChallenge) {
        this.totalSpent = consumptionChallenge.getConsumptions().stream()
                .mapToInt(consumption -> Math.toIntExact(consumption.getAmount()))
                .sum();
        this.targetAmount = consumptionChallenge.getTargetAmount();
        this.succeed = totalSpent <= targetAmount;
        this.startDate = consumptionChallenge.getStartDate();
        this.endDate = consumptionChallenge.getEndDate();
        this.title = calculateTitle();
        this.prize = consumptionChallenge.getPrize();
    }

    private String calculateTitle() {
        int month = startDate.getMonthValue();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int weekOfMonth = startDate.get(weekFields.weekOfMonth());

        return month + "-" + weekOfMonth;
    }
}
