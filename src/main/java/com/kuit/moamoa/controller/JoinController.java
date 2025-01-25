package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.JoinDTO;
import com.kuit.moamoa.global.response.ErrorResponse;
import com.kuit.moamoa.global.response.ExceptionResponseStatus;
import com.kuit.moamoa.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 로그인", description = "사용자 회원가입 및 로그인")
@RestController
@Slf4j
@RequestMapping("/login")
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @Operation(summary = "유저 로그인 & 회원가입", description = "회원가입 최초 페이지 경로입니다.")
    @PostMapping()
    public ErrorResponse login(@ModelAttribute JoinDTO joinDTO) {
        joinService.JoinProcess(joinDTO);
        log.info("DTO:{}",joinDTO);
        return new ErrorResponse(ExceptionResponseStatus.SUCCESS);
    }

    @Operation(summary = "유저 회원가입", description = "서비스 내 간편 회원가입 경로입니다.")
    @PostMapping("/signup")
    public ErrorResponse signUp(@RequestBody JoinDTO joinDTO){
        joinService.JoinProcess(joinDTO);
        return new ErrorResponse(ExceptionResponseStatus.SUCCESS);
    }

}
