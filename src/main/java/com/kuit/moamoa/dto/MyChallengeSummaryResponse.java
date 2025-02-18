package com.kuit.moamoa.dto;

import com.kuit.moamoa.domain.Challenge;
import com.kuit.moamoa.domain.ChallengeProgress;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;

@Getter
public class MyChallengeSummaryResponse {
    int totalEarned;
    long successRate;
    int top;
    int totalTries;
    int totalSucceed;
    List<ChallengeRecordResponse> challengeRecords;

    public MyChallengeSummaryResponse(List<Challenge> challenges, List<ChallengeProgress> challengeProgresses) {
        this.totalEarned = calculateTotalEarned(challenges, challengeProgresses);
        this.successRate = calculateTotalSucceed(challengeProgresses) / challenges.size();
        this.top = (int) (successRate * 0.9);
        this.totalTries = challenges.size();
        this.totalSucceed = calculateTotalSucceed(challengeProgresses);
        this.challengeRecords = IntStream.range(0, challenges.size())
                .mapToObj(i -> new ChallengeRecordResponse(challenges.get(i), challengeProgresses.get(i))).toList();
    }

    private int calculateTotalEarned(List<Challenge> challenges, List<ChallengeProgress> challengeProgresses) {
        return IntStream.range(0, challenges.size())
                .map(i -> {
                    if (challengeProgresses.get(i).isGoalAchieved()) {
                        return challenges.get(i).getBattleCoin();
                    }
                    return -challenges.get(i).getBattleCoin();
                })
                .sum();
    }

    private int calculateTotalSucceed(List<ChallengeProgress> challengeProgresses) {
        return (int) challengeProgresses.stream()
                .filter(ChallengeProgress::isGoalAchieved)
                .count();
    }


    @Getter
    static class ChallengeRecordResponse {
        LocalDate startDate;
        LocalDate endDate;
        Long challengeId;
        String title;
        boolean succeed;
        int transaction;

        public ChallengeRecordResponse(Challenge challenge, ChallengeProgress challengeProgress) {
            this.startDate = LocalDate.from(challenge.getStartDate());
            this.endDate = LocalDate.from(challenge.getEndDate());
            this.challengeId = challenge.getId();
            this.transaction = challenge.getBattleCoin();
            this.succeed = challengeProgress.isGoalAchieved();
            this.title = challenge.getTitle();
        }
    }
}
