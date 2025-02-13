package com.kuit.moamoa.service;

import com.kuit.moamoa.dto.request.OverspendingDiagnosisRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OverspendingDiagnosisService {
    public String diagnoseOverspending(OverspendingDiagnosisRequest request) {

        double monthlyIncome = request.getMonthlyIncome(); //월 소득 금액
        double lastMonthSpending = request.getLastMonthSpending(); //지난 달 소비액
        double averageMonthlySavings = request.getAverageMonthlySavings(); //월 평균 저축액
        Integer ageGroup = request.getAgeGroup(); //연령대

        double spendingRatio = (monthlyIncome - averageMonthlySavings) / monthlyIncome;

        switch (ageGroup) {
            case 0: // 10-20대
                if (spendingRatio > 0.5) return "과소비";
                else return "적정소비";
            case 1: // 30대
                if (spendingRatio > 0.7) return "과소비";
                else return "적정소비";
            case 2: // 40대
                if (spendingRatio > 0.8) return "과소비";
                else return "적정소비";
            case 3: // 50대 이상
                if (spendingRatio > 0.9) return "과소비";
                else return "적정소비";
            default:
                return "연령대 입력 오류";
        }
    }
}
