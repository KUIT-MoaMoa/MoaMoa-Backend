package com.kuit.moamoa.join.controller;

import com.kuit.moamoa.global.jwt.Jwt;
import com.kuit.moamoa.global.jwt.JWTUtil;
import com.kuit.moamoa.join.dto.ResetPasswordRequest;
import com.kuit.moamoa.join.dto.UserAuthRequest;
import com.kuit.moamoa.join.dto.NicknameRequest;
import com.kuit.moamoa.join.oauth2.dto.UserAuthResponse;
import com.kuit.moamoa.configuration.response.ApiResponse;
import com.kuit.moamoa.join.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "사용자 로그인", description = "사용자 회원가입 및 로그인")
@RestController
@Slf4j
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;
    private final JWTUtil jwtUtil;

    @Operation(summary = "유저 일반 로그인", description = "서비스 내 간편 로그인 경로입니다. Authorization 헤더의 JWT토큰이 유효해야 작동합니다.")
    @PostMapping("/login")
    public String login(@ModelAttribute UserAuthRequest request) {
        Long userId = joinService.login(request.getEmail(), request.getPassword());
        if(userId == 0) {
            return "redirect:https://moa-moa-frontend-individual.vercel.app/login";
        }
        String token = jwtUtil.createJwt(userId, "ADMIN");
        return "redirect:https://moa-moa-frontend-individual.vercel.app?token="+token;
    }


    @Operation(summary = "유저 일반 회원가입", description = "서비스 내 간편 회원가입: 닉네임 설정 전, 가입 완료 경로입니다.")
    @PostMapping("/join")
    public ApiResponse<UserAuthResponse> join(@RequestBody UserAuthRequest request){
        UserAuthResponse userAuthResponse = joinService.joinProcess(request);
        return new ApiResponse<>(userAuthResponse);

    }


    @Operation(summary = "비밀번호 변경", description = "비밀번호 재설정 경로입니다.")
    @PostMapping("/resetPassword")
    public ApiResponse<String> resetPassword(@Jwt Long userId, @RequestBody ResetPasswordRequest request, @RequestHeader Map<String, String> headers) throws Exception {
        joinService.resetPassword(userId, request);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
        }
        return new ApiResponse<>("비밀번호가 성공적으로 변경되었습니다.");
    }

    @Operation(summary = "닉네임 설정", description = "일반/소셜 회원가입 한 유저들이 닉네임을 설정하는 경로입니다.")
    @PostMapping("/nickname")
    public ApiResponse<String> setNickname(@RequestBody NicknameRequest request){
        joinService.setNickname(request);
        return new ApiResponse<>("닉네임이 성공적으로 설정되었습니다.");
    }

}
