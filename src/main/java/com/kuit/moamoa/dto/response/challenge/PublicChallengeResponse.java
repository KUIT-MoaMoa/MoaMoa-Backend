package com.kuit.moamoa.dto.response.challenge;

import com.kuit.moamoa.domain.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PublicChallengeResponse {
    private Long challengeId;
    private String title;
    private String content;
    private Long duration;
    private Integer battleCoin;
    private Boolean publicChallenge;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer participantCount;
    private LocalDateTime recruitmentDeadline;

    public PublicChallengeResponse(Challenge challenge) {
        this.challengeId = challenge.getId();
        this.title = challenge.getTitle();
        this.content = challenge.getContent();
        this.duration = challenge.getDuration();
        this.battleCoin = challenge.getBattleCoin();
        this.publicChallenge = challenge.getPublicChallenge();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
        this.participantCount = challenge.getProgressList().size();
        this.recruitmentDeadline = challenge.getRecruitmentDeadline();
    }
}