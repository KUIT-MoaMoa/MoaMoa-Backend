package com.kuit.moamoa.dto;

import com.kuit.moamoa.domain.ChallengeCategory;
import com.kuit.moamoa.domain.ConsumptionCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddMyConsumptionRequest {
    ConsumptionCategory consumptionCategory;
    ChallengeCategory challengeCategory;
    int amount;
}
