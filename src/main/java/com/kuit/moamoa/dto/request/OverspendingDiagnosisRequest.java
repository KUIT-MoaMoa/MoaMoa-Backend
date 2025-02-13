package com.kuit.moamoa.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor
public class OverspendingDiagnosisRequest {
    private double monthlyIncome; //월 소득 금액
    private double lastMonthSpending; //지난 달 소비액
    private double averageMonthlySavings; //월 평균 저축액
    private Integer ageGroup; //연령대

}
