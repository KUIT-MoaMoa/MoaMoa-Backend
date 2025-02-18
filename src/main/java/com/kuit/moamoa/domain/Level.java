package com.kuit.moamoa.domain;

import lombok.Getter;

@Getter
public enum Level { // TODO: image file 넣기
    LEVEL_0(0, "https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/character/Lv.0.png"),
    LEVEL_1(200, "https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/character/Lv.1.png"),
    LEVEL_2(500, "https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/character/Lv.2.png"),
    LEVEL_3(1000, "https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/character/Lv.3.png"),
    LEVEL_4(1700, "https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/character/Lv.4.png"),
    LEVEL_5(2600, "https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/character/Lv.5.png"),
    LEVEL_6(3600, "https://moamoa-backend.s3.ap-northeast-2.amazonaws.com/character/Lv.6.png");

    private final int leastRequired;
    private final String imageUrl;

    Level(int leastRequired, String imageUrl) {
        this.leastRequired = leastRequired;
        this.imageUrl = imageUrl;
    }

    public static Level get(int coin) {
        Level ret = LEVEL_0;
        for(Level level : Level.values()) {
            if(level.leastRequired > coin) {
                return ret;
            }
            ret = level;
        }
        return LEVEL_6;
    }

    public static Level getNextLevel(int coin) {
        return get(coin).nextLevel();
    }

    private Level nextLevel() {
        return Level.values()[Math.min(this.ordinal() + 1, Level.values().length)];
    }
}
