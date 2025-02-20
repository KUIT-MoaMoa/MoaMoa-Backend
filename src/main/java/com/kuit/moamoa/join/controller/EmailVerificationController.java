package com.kuit.moamoa.join.controller;

import com.kuit.moamoa.join.dto.EmailVerificationRequest;
import com.kuit.moamoa.configuration.response.ApiResponse;
import com.kuit.moamoa.join.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/verify-email")
@Slf4j
@RequiredArgsConstructor
@Tag(name="이메일 인증", description = "소셜 로그인 연동이 안될 경우 이메일 인증을 진행하는 경로입니다.")
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send")
    @Operation(summary = "인증받을 이메일 입력")
    @ResponseBody
    public ApiResponse<String > mailSend(@RequestBody EmailVerificationRequest userMail) {
        emailVerificationService.sendMail(userMail);
        return new ApiResponse<>("메일이 전송되었습니다.");

    }

     //인증번호 일치여부 확인
    @GetMapping("/check")
    @Operation(summary = "인증번호 확인", description = "서버에서 인증번호를 확인하고 바로 성공 화면으로 리다이렉트 합니다.")
    public String verifyEmail(@RequestParam String token, Model model) {
        boolean isVerified = emailVerificationService.checkMail(token);
        model.addAttribute("isVerified", isVerified);
        if (isVerified) {
            return "verification_success"; // 성공 화면
        } else {
            return "verification_fail"; // 실패 화면
        }
    }
//    @GetMapping("/check")
//    public Map<String, Boolean> mailCheck(@RequestParam String token) {
//        boolean isVerified = emailVerificationService.checkMail(token);
//        return Collections.singletonMap("isVerified", isVerified);
//    }


    @GetMapping("/result")
    @ResponseBody
    public boolean mailResult() {
        return emailVerificationService.getVerificationStatus();
    }
}
