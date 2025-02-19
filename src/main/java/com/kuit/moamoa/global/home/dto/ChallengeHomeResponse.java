package com.kuit.moamoa.global.home.dto;

import com.kuit.moamoa.social.challenge.domain.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

// 참여 중인 챌린지와 모집 중인 챌린지를 포함한 응답 DTO
public class ChallengeHomeResponse {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipatingChallengeResponse {
        private Long challengeId;
        private String title;
        private Double usageRate; // 사용 비율(퍼센트로 표현)
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitingChallengeResponse {
        private Long challengeId;
        private String title;
        private String remainingDays; // "D-7" 형식으로 표현
        private String participantCountRate;

        public static RecruitingChallengeResponse from(Challenge challenge) {
            LocalDateTime now = LocalDateTime.now();
            long daysUntilStart = ChronoUnit.DAYS.between(now.toLocalDate(), challenge.getRecruitmentDeadline().toLocalDate());
            String remainingDays = "D-" + Math.max(0, daysUntilStart);

            return RecruitingChallengeResponse.builder()
                    .challengeId(challenge.getId())
                    .title(challenge.getTitle())
                    .remainingDays(remainingDays)
                    .participantCountRate(challenge.getProgressList().size() + "/" + challenge.getHeadCount())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeHomeSummaryResponse {
        private boolean hasParticipatingChallenges;
        private List<ParticipatingChallengeResponse> participatingChallenges;
        private List<RecruitingChallengeResponse> recruitingChallenges;
    }
}