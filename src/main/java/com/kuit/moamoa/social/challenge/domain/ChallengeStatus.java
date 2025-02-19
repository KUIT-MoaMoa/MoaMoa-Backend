package com.kuit.moamoa.social.challenge.domain;

import lombok.Getter;

@Getter
public enum ChallengeStatus {
    RECRUITING,    // 모집 중
    ONGOING,       // 진행 중
    COMPLETED,    // 완료됨
    CANCELED       // 취소됨
}
