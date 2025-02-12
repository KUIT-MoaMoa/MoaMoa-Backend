package com.kuit.moamoa.dto;

import com.kuit.moamoa.domain.ChallengeRecord;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class MyChallengeSummaryResponse {
    int totalEarned;
    long successRate;
    int top;
    int totalTries;
    int totalSucceed;
    List<ChallengeRecordResponse> challengeRecords;

    public MyChallengeSummaryResponse(int totalEarned, long successRate, int top, int totalTries, int totalSucceed,
                                      List<ChallengeRecord> challengeRecords) {
        this.totalEarned = totalEarned;
        this.successRate = successRate;
        this.top = top;
        this.totalTries = totalTries;
        this.totalSucceed = totalSucceed;
        this.challengeRecords = challengeRecords.stream()
                .map(ChallengeRecordResponse::new)
                .toList();
    }

    @Getter
    static class ChallengeRecordResponse {
        LocalDate startDate;
        LocalDate endDate;
        String title;
        boolean succeed;
        int transaction;

        public ChallengeRecordResponse(ChallengeRecord challengeRecord) {
            this.startDate = challengeRecord.getStartDate();
            this.endDate = challengeRecord.getEndDate();
            this.transaction = Math.toIntExact(challengeRecord.getTransaction());
            this.succeed = this.transaction > 0;
            this.title = challengeRecord.getTitle();
        }
    }
}
