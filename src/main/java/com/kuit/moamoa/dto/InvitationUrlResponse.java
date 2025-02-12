package com.kuit.moamoa.dto;

import lombok.Getter;

@Getter
public class InvitationUrlResponse {
    String url;

    public InvitationUrlResponse(String url) {
        this.url = url;
    }
}
