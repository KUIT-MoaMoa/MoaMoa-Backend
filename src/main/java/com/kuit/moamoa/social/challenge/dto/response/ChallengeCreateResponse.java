package com.kuit.moamoa.social.challenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChallengeCreateResponse {
    private Long challengeId;
    private String message;
}