package com.kuit.moamoa.dto.response.challenge;

import com.kuit.moamoa.domain.Challenge;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PublicChallengeResponse {
    private String title;
    private String content;
    private Integer duration;
    private Integer battleCoin;
    private Integer participantCount;
    private LocalDateTime recruitmentDeadline;

    public PublicChallengeResponse(Challenge challenge) {
        this.title = challenge.getTitle();
        this.content = challenge.getContent();
        this.duration = challenge.getDuration();
        this.battleCoin = challenge.getBattleCoin();
        this.participantCount = challenge.getProgressList().size();
        this.recruitmentDeadline = challenge.getRecruitmentDeadline();
    }
}