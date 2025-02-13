package com.kuit.moamoa.dto.response.challenge;

import com.kuit.moamoa.domain.ChallengeProgress;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

// 전체 응답을 담을 DTO
@Getter
@Builder
public class ChallengeMemberProgressResponse {
    private UserProgressResponse userProgress;
    private List<OtherMemberProgressResponse> otherMembersProgress;
}