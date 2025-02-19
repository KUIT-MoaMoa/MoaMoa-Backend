package com.kuit.moamoa.global.home.dto;

import static java.lang.Math.max;

import com.kuit.moamoa.user.domain.Level;
import lombok.Getter;

@Getter
public class CoinSummary {
    private Level level;
    private int coinNeeded;

    public CoinSummary(int coin) {
        this.level = Level.get(coin);
        this.coinNeeded = needForNextLevel(coin);
    }

    private int needForNextLevel(int coin) {
        int leastRequired = Level.getNextLevel(coin).getLeastRequired();

        return max(0, leastRequired - coin);
    }
}
