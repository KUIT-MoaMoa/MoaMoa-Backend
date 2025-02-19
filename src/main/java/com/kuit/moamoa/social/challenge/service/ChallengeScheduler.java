package com.kuit.moamoa.social.challenge.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChallengeScheduler {
    private final ChallengeService challengeService;

    // 매일 자정에 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void checkChallengeStatus() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Checking challenge status at {}", now);

        // 시작해야 할 챌린지 처리
        challengeService.startChallengesForDate(now);

        // 종료해야 할 챌린지 처리
        challengeService.completeChallengesForDate(now);
    }
}
