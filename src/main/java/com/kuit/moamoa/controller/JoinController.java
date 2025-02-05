package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.request.UserAuthRequest;
import com.kuit.moamoa.dto.request.NicknameRequest;
import com.kuit.moamoa.dto.response.UserAuthResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 로그인", description = "사용자 회원가입 및 로그인")
@RestController
@Slf4j
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @Operation(summary = "유저 일반 로그인", description = "서비스 내 간편 로그인 경로입니다.")
    @PostMapping("/login")
    public String login(@ModelAttribute UserAuthRequest request) {
        return "ok";
    }

    @Operation(summary = "유저 일반 회원가입", description = "서비스 내 간편 회원가입: 닉네임 설정 전, 가입 완료 경로입니다.")
    @PostMapping("/join")
    public ResponseEntity<UserAuthResponse> join(@ModelAttribute UserAuthRequest request){
        return ResponseEntity.ok(joinService.joinProcess(request));

    }


    @Operation(summary = "비밀번호 변경", description = "비밀번호 재설정 경로입니다.")
    @PostMapping("/resetPassword")
    public ApiResponse<String> resetPassword(@RequestBody UserAuthRequest request){
        joinService.resetPassword(request);
        return new ApiResponse<>("비밀번호가 성공적으로 변경되었습니다.");
    }

    @Operation(summary = "닉네임 설정", description = "소셜 로그인으로 회원가입 한 유저들이 닉네임을 설정하는 경로입니다.")
    @PostMapping("/nickname")
    public String setNickname(@RequestBody NicknameRequest request){
        joinService.setNickname(request);
        return "ok";
    }

}
