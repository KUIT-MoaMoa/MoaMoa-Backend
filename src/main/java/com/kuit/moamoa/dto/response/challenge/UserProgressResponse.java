package com.kuit.moamoa.dto.response.challenge;

import com.kuit.moamoa.domain.ChallengeProgress;
import lombok.Builder;
import lombok.Getter;

// 로그인한 사용자의 진행률 응답 DTO
@Getter
@Builder
public class UserProgressResponse {
    private double usedRate;

    public static UserProgressResponse from(ChallengeProgress progress) {
        return UserProgressResponse.builder()
                .usedRate(progress.getUsagePercentage())
                .build();
    }
}