package com.kuit.moamoa.user.dto;

import com.kuit.moamoa.user.domain.Level;
import com.kuit.moamoa.user.domain.User;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.Getter;

@Getter
public class UserPageResponse {
    String nickname;
    String dustImage;
    String boarderUrl;
    int level;
    int beenWith;
    int coin;

    public UserPageResponse(User user) {
        this.nickname = user.getNickname();
        this.dustImage = Level.get(user.getCoin()).getImageUrl();
        this.boarderUrl = user.getBoarderUrl();
        this.level = Level.get(user.getCoin()).ordinal();
        this.beenWith = (int) ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.from(user.getCreatedAt()));
        this.coin = user.getCoin();
    }
}
