package com.kuit.moamoa.dto.request.challenge;

import com.kuit.moamoa.domain.ChallengeCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChallengeCreateRequest {
    @NotNull private String title;
    @NotNull private String content;
    @NotNull private Integer headCount;
    @NotNull private LocalDateTime startDate;
    @NotNull private Integer duration;
    @NotNull private Integer battleCoin;
    @NotNull private Boolean publicChallenge;
    @NotNull private ChallengeCategory challengeCategory;
    @NotNull private Integer goalAmount;
}
