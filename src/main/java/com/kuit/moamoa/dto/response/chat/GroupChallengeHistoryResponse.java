package com.kuit.moamoa.dto.response.chat;

import com.kuit.moamoa.domain.ChallengeStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GroupChallengeHistoryResponse {
    private Long challengeId;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer battleCoin;
    private Integer participantCount;
    private Boolean isSuccessful;  // 현재 로그인한 유저의 성공 여부
    private ChallengeStatus status;
}
