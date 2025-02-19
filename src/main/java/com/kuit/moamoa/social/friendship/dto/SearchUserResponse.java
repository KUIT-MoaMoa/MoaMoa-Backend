package com.kuit.moamoa.social.friendship.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchUserResponse {
    private Long userId;
    private String nickname;
    private String profileImageUrl;
    private String borderImageUrl;
    private Boolean isFriend;
    private Boolean isInSameChallenge;
}