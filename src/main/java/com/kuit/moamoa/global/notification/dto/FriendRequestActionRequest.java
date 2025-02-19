package com.kuit.moamoa.global.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestActionRequest {
    private boolean accept; // true면 수락, false면 거절
}