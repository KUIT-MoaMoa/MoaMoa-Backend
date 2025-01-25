package com.kuit.moamoa.controller;

import com.kuit.moamoa.global.response.ErrorResponse;
import com.kuit.moamoa.global.response.ExceptionResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 로그인", description = "사용자 회원가입 및 로그인")
@RestController
@Slf4j
//@RequestMapping("/auth")
public class AuthController {
    @Operation(summary = "유저 로그인 & 회원가입", description = "회원가입 최초 페이지 경로입니다.")
    @GetMapping()
    public ErrorResponse login() {
//        LocalDateTime localDateTime = LocalDateTime.now();
        return new ErrorResponse(ExceptionResponseStatus.SUCCESS);
    }

}
