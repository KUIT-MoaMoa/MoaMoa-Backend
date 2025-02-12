package com.kuit.moamoa.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConsumptionChallengeResponse {
    int prize;
    LocalDate startDate;
    LocalDate endDate;
    int targetAmount;
}
