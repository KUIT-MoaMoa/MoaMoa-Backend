package com.kuit.moamoa.dto.request.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestActionRequest {
    private boolean accept; // true면 수락, false면 거절
}