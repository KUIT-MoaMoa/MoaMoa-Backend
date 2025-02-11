package com.kuit.moamoa.dto.response.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChallengeCreateResponse {
    private Long challengeId;
    private String message;
}