package com.kuit.moamoa.controller;

import com.kuit.moamoa.global.response.ErrorResponse;
import com.kuit.moamoa.global.response.ExceptionResponseStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Tag(name = "사용자 로그인", description = "사용자 회원가입 및 로그인")
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public ErrorResponse login() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return new ErrorResponse(ExceptionResponseStatus.SUCCESS);
    }

}
