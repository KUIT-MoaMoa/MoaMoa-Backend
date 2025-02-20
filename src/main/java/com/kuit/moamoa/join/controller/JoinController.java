package com.kuit.moamoa.join.controller;

import com.kuit.moamoa.join.dto.EmailVerificationRequest;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 로그인", description = "사용자 회원가입 및 로그인")
@Controller
@Slf4j
@RequiredArgsConstructor
public class JoinController {

    private final JoinService joinService;

    @Operation(summary = "유저 일반 로그인", description = "서비스 내 간편 로그인 경로입니다. Authorization 헤더의 JWT토큰이 유효해야 작동합니다.")
    @PostMapping("/login")
    @ResponseBody
    public ApiResponse<String> login(@ModelAttribute UserAuthRequest request) {
        return new ApiResponse<>("로그인이 완료되었습니다.");
    }


    @Operation(summary = "유저 일반 회원가입", description = "서비스 내 간편 회원가입: 닉네임 설정 전, 가입 완료 경로입니다.")
    @PostMapping("/join")
    @ResponseBody
    public ApiResponse<UserAuthResponse> join(@RequestBody UserAuthRequest request){
        UserAuthResponse userAuthResponse = joinService.joinProcess(request);
        return new ApiResponse<>(userAuthResponse);

    }


    @Operation(summary = "비밀번호 변경을 위한 인증 메일 전송")
    @PostMapping("/send")
    @ResponseBody
    public ApiResponse<String> sendMail(@RequestBody EmailVerificationRequest request) throws Exception {
        joinService.sendEmailForPassword(request);
//        for (Map.Entry<String, String> entry : headers.entrySet()) {
//            System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
//        }
        return new ApiResponse<>("메일이 전송되었습니다.");
    }

    @Operation(summary = "비밀번호 변경")
    @PostMapping("/reset-password")
    @ResponseBody
    public ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest request){
        joinService.resetPassword(request);
        return new ApiResponse<>("비밀번호가 성공적으로 변경되었습니다.");
    }

    //인증번호 일치여부 확인
    @GetMapping("/check")
    @Operation(summary = "인증번호 확인", description = "서버에서 인증번호를 확인하고 바로 성공 화면으로 리다이렉트 합니다.")
    public String verifyEmail(@RequestParam String token) {
        boolean isVerified = joinService.checkMail(token);
        log.info("{}",isVerified);
        if (isVerified) {
            return "password_verification_success"; // 성공 화면
        } else {
            return "password_verification_fail"; // 실패 화면
        }
    }

    //비밀번호 변경을 위한 이메일 인증 결과 반환
    @GetMapping("/result")
    @ResponseBody
    public boolean mailResult() {
        return joinService.getVerificationStatus();
    }

    @Operation(summary = "닉네임 설정", description = "일반/소셜 회원가입 한 유저들이 닉네임을 설정하는 경로입니다.")
    @PostMapping("/nickname")
    @ResponseBody
    public ApiResponse<String> setNickname(@RequestBody NicknameRequest request){
        joinService.setNickname(request);
        return new ApiResponse<>("닉네임이 성공적으로 설정되었습니다.");
    }

}
