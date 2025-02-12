package com.kuit.moamoa.dto.response.challenge;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserOngoingChallengeResponse {
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;  // 남은 기한
    private Integer duration;    // 진행 기간
    private Integer participantCount;  // 참여중인 유저 수

    @Builder
    public UserOngoingChallengeResponse(String title, LocalDateTime startDate, LocalDateTime endDate,
                                        Integer duration, Integer participantCount) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = duration;
        this.participantCount = participantCount;
    }
}
