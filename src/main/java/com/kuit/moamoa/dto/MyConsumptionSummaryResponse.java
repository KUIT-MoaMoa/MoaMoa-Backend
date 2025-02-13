package com.kuit.moamoa.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyConsumptionSummaryResponse {
    long successRate;
    int top;
    int totalTries;
    int totalSucceed;
    List<Stat> stats;
    TotalSpent totalSpent;

    @Getter
    @AllArgsConstructor
    public static class Stat {
        String range;
        int targetAmount;
        int totalSpent;
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

        public TotalSpent(int fixed, int beauty, int activity, int living, int celebration, int etc) {
            this.fixed = fixed;
            this.beauty = beauty;
            this.activity = activity;
            this.living = living;
            this.celebration = celebration;
            this.etc = etc;
            this.total = fixed + beauty + activity + living + celebration + etc;
        }
    }
}
