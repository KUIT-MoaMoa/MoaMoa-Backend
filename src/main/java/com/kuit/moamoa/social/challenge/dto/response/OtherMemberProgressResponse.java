package com.kuit.moamoa.social.challenge.dto.response;

import com.kuit.moamoa.social.challenge.domain.ChallengeProgress;
import lombok.Builder;
import lombok.Getter;

// 다른 참여자들의 정보 응답 DTO
@Getter
@Builder
public class OtherMemberProgressResponse {
    private String userName;
    private String profileImage;
    private double usedRate;

    public static OtherMemberProgressResponse from(ChallengeProgress progress) {
        return OtherMemberProgressResponse.builder()
                .userName(progress.getUser().getNickname())
                .profileImage(progress.getUser().getImageUrl())
                .usedRate(progress.getUsagePercentage())
                .build();
    }
}
