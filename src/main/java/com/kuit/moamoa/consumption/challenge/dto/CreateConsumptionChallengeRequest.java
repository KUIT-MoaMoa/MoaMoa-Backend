package com.kuit.moamoa.consumption.challenge.dto;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class CreateConsumptionChallengeRequest {
    LocalDate startDate;
    LocalDate endDate;
    int targetAmount;
}
