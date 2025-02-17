package com.kuit.moamoa.dto.request;

import com.kuit.moamoa.domain.Status;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserAuthRequest {

    private String email;
    private String password;
//    private Status status;

}
