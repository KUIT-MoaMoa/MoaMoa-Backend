package com.kuit.moamoa.social.challenge.dto.response;

import com.kuit.moamoa.social.challenge.domain.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CompletedChallengeResponse {
    private Long challengeId;
    private String title;
    private int battleCoin;
    private LocalDateTime endDate;
    private ChallengeStatus status;
    private boolean rewardClaimed;
    private boolean isGoalAchieved;
}