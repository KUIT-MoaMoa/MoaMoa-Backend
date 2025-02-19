package com.kuit.moamoa.user.dto.response;

import lombok.Getter;

@Getter
public class InvitationUrlResponse {
    String url;

    public InvitationUrlResponse(String url) {
        this.url = url;
    }
}
