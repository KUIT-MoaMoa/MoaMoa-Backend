package com.kuit.moamoa.controller;

import com.kuit.moamoa.domain.Level;
import com.kuit.moamoa.domain.User;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.Getter;

@Getter
public class UserPageResponse {
    String nickname;
    String dustImage;
    int level;
    int beenWith;
    int coin;

    public UserPageResponse(User user) {
        this.nickname = user.getNickname();
        this.dustImage = Level.get(user.getCoin()).getImageUrl();
        this.level = Level.get(user.getCoin()).ordinal();
        this.beenWith = (int) ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.from(user.getCreatedAt()));
        this.coin = user.getCoin();
    }
}
