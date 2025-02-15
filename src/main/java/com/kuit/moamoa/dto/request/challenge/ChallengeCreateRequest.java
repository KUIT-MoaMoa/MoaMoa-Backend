package com.kuit.moamoa.dto.request.challenge;

import com.kuit.moamoa.domain.ChallengeCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class ChallengeCreateRequest {
    @NotNull private String title;
    @NotNull private String content;
    @NotNull private Integer headCount;
    @NotNull private String startDate;  // String으로 변경
    @NotNull private String endDate;    // endDate 추가
    @NotNull private Integer battleCoin;
    @NotNull private Boolean publicChallenge;
    @NotNull private ChallengeCategory challengeCategory;
    @NotNull private Integer goalAmount;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocalDateTime getStartDate() {
        return LocalDateTime.parse(this.startDate + "T00:00:00");
    }

    public LocalDateTime getEndDate() {
        return LocalDateTime.parse(this.endDate + "T23:59:59");
    }
}
