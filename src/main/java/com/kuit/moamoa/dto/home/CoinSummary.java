package com.kuit.moamoa.dto.home;

import static java.lang.Math.max;

import com.kuit.moamoa.domain.Level;
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
