package com.kuit.moamoa.social.challenge.dto.response;

import com.kuit.moamoa.social.challenge.domain.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserOngoingChallengeResponse {
    private Long challengeId;
    private String title;
    private String content;
    private Boolean publicChallenge;
    private LocalDateTime startDate;
    private LocalDateTime endDate;  // 남은 기한
    private Long duration;    // 진행 기간
    private Integer battleCoin;
    private Boolean isParticipating;
    private Integer participantCount;  // 참여중인 유저 수
    private ChallengeStatus status;

    @Builder
    public UserOngoingChallengeResponse(Long challengeId, String title, String content, Boolean publicChallenge, LocalDateTime startDate, LocalDateTime endDate,
                                        Long duration, Integer battleCoin, Boolean isParticipating, Integer participantCount, ChallengeStatus status) {
        this.challengeId = challengeId;
        this.title = title;
        this.content = content;
        this.publicChallenge = publicChallenge;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.battleCoin = battleCoin;
        this.isParticipating = isParticipating;
        this.participantCount = participantCount;
        this.status = status;
    }
}
