package com.kuit.moamoa.domain;

import lombok.Getter;

@Getter
public enum Level { // TODO: image file 넣기
    LEVEL_0(0, "image_url"),
    LEVEL_1(200, " "),
    LEVEL_2(500, " "),
    LEVEL_3(1000, " "),
    LEVEL_4(1700, " "),
    LEVEL_5(2600, " "),
    LEVEL_6(3600, " ");

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
}
