package com.kuit.moamoa.user.dto.response;

import com.kuit.moamoa.consumption.record.domain.Consumption;
import com.kuit.moamoa.consumption.record.domain.ConsumptionCategory;
import com.kuit.moamoa.consumption.challenge.domain.ConsumptionChallenge;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyConsumptionSummaryResponse {
    double successRate;
    int top;
    int totalTries;
    int totalSucceed;
    List<Stat> stats;
    TotalSpent totalSpent;

    public MyConsumptionSummaryResponse(List<ConsumptionChallenge> consumptionChallenges, List<Consumption> consumptions) {
        this.totalTries = consumptionChallenges.size();
        this.totalSucceed = (int) consumptionChallenges.stream()
                .filter(this::calculateSucceed)
                .count();
        if(totalTries == 0) {
            this.successRate = 0L;
        }
        else {
            this.successRate = (double) this.totalSucceed / this.totalTries;
        }
        this.top = calculateTop();
        this.stats = consumptionChallenges.stream()
                .map(Stat::new)
                .toList();
        this.totalSpent = new TotalSpent(consumptions);
    }

    private int calculateTop() {
        return (int) (successRate * 100 * 0.90);
    }

    private boolean calculateSucceed(ConsumptionChallenge consumptionChallenge) {
        return calculateTotalSpent(consumptionChallenge)<= consumptionChallenge.getTargetAmount();
    }

    private int calculateTotalSpent(ConsumptionChallenge consumptionChallenge) {
        return consumptionChallenge.getConsumptions().stream()
                .mapToInt(consumption -> Math.toIntExact(consumption.getAmount()))
                .sum();
    }

    @Getter
    @AllArgsConstructor
    public static class Stat {
        String range;
        int targetAmount;
        int totalSpent;

        public Stat(ConsumptionChallenge consumptionChallenge) {
            LocalDate startDate = consumptionChallenge.getStartDate();
            this.range = extractRange(startDate);
            this.targetAmount = consumptionChallenge.getTargetAmount();
            this.totalSpent = consumptionChallenge.getConsumptions().stream()
                    .mapToInt(consumption -> Math.toIntExact(consumption.getAmount()))
                    .sum();
        }
        private String extractRange(LocalDate startDate) {

            int month = startDate.getMonthValue();
            WeekFields weekFields = WeekFields.of(Locale.getDefault());

            int weekOfMonth = startDate.get(weekFields.weekOfMonth());

            return month + "-" + weekOfMonth;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class TotalSpent {
        int fixed;
        int beauty;
        int activity;
        int living;
        int celebration;
        int etc;
        int total;

        public TotalSpent(List<Consumption> consumptions) {
            this.fixed = calculateCategory(consumptions, ConsumptionCategory.FIXED);
            this.beauty = calculateCategory(consumptions, ConsumptionCategory.BEAUTY);
            this.activity = calculateCategory(consumptions, ConsumptionCategory.ACTIVITY);
            this.living = calculateCategory(consumptions, ConsumptionCategory.LIVING);
            this.celebration = calculateCategory(consumptions, ConsumptionCategory.CELEBRATION);
            this.etc = calculateCategory(consumptions, ConsumptionCategory.ETC);
            this.total = fixed + beauty + activity + living + celebration + etc;
        }

        private int calculateCategory(List<Consumption> consumptions, ConsumptionCategory category) {
            return consumptions.stream()
                    .filter(consumption -> consumption.getConsumptionCategory().equals(category))
                    .mapToInt(consumption -> Math.toIntExact(consumption.getAmount()))
                    .sum();
        }
    }
}
