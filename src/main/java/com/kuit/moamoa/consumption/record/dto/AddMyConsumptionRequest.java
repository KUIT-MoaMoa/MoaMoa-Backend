package com.kuit.moamoa.consumption.record.dto;

import com.kuit.moamoa.social.challenge.domain.ChallengeCategory;
import com.kuit.moamoa.consumption.record.domain.ConsumptionCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddMyConsumptionRequest {
    ConsumptionCategory consumptionCategory;
    ChallengeCategory challengeCategory;
    int amount;
}
