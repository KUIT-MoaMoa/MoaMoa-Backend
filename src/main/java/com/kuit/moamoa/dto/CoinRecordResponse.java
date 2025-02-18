package com.kuit.moamoa.dto;

import com.kuit.moamoa.domain.ChallengeProgress;
import com.kuit.moamoa.domain.ConsumptionChallenge;
import com.kuit.moamoa.domain.Level;
import com.kuit.moamoa.domain.User;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CoinRecordResponse {
    private int coin;
    private String nickname;
    private String dustUrl;
    List<Transaction> transactions;

    public CoinRecordResponse(User user, List<ChallengeProgress> progresses,
                              List<ConsumptionChallenge> consumptionChallenges) {

        this.coin = user.getCoin();
        this.nickname = user.getNickname();
        this.dustUrl = Level.get(coin).getImageUrl();
        this.transactions = concatTransaction(
                getChallengeTransaction(progresses),
                getConsumptionChallengeTransaction(consumptionChallenges)
        );
    }

    private List<Transaction> getConsumptionChallengeTransaction(List<ConsumptionChallenge> consumptionChallenges) {

        return consumptionChallenges.stream()
                .filter(ConsumptionChallenge::isAchieved)
                .map(consumptionChallenge -> new Transaction(
                        calculateTitle(consumptionChallenge.getStartDate()),
                        consumptionChallenge.getUpdatedAt().toLocalDate(),
                        consumptionChallenge.getPrize()
                ))
                .collect(Collectors.toList());
    }


    private List<Transaction> getChallengeTransaction(List<ChallengeProgress> challengeProgresses) {

        return challengeProgresses.stream()
                .filter(ChallengeProgress::isRewardClaimed)
                .map(challengeProgress -> new Transaction(
                        challengeProgress.getChallenge().getTitle(),
                        challengeProgress.getUpdatedAt().toLocalDate(),
                        challengeProgress.getChallenge().getBattleCoin()
                ))
                .collect(Collectors.toList());
    }

    private List<Transaction> concatTransaction(List<Transaction> challengeTransactions,
                                                List<Transaction> consumptionChallengeTransaction) {
        return transactions = Stream.concat(challengeTransactions.stream(),
                        consumptionChallengeTransaction.stream())
                .toList();
    }


    private String calculateTitle(LocalDate startDate) {
        int month = startDate.getMonthValue();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int weekOfMonth = startDate.get(weekFields.weekOfMonth());

        return month + "-" + weekOfMonth + "주차 소비 챌린지";
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Transaction {
        private String title;
        private LocalDate date;
        private int transaction;
    }
}
