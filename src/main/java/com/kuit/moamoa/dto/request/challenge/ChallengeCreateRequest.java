package com.kuit.moamoa.dto.request.challenge;

import com.kuit.moamoa.domain.ChallengeCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChallengeCreateRequest {
    private String title;
    private String content;
    private Integer headCount;
    private Integer duration;
    private Integer battleCoin;
    private Boolean publicChallenge;
    private ChallengeCategory challengeCategory;
    private Integer goalAmount;
}