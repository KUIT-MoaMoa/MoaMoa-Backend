package com.kuit.moamoa.controller;

import com.kuit.moamoa.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping("/verify-email")
@Slf4j
@RequiredArgsConstructor
@Tag(name="이메일 인증", description = "소셜 로그인 연동이 안될 경우 이메일 인증을 진행하는 경로입니다.")
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send")
    @ResponseBody
    public HashMap<String, Object> mailSend(String userMail) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            int number = emailVerificationService.sendMail(userMail);
            String num = String.valueOf(number);

            map.put("success", Boolean.TRUE);
            map.put("number", num);
        } catch (Exception e) {
            map.put("success", Boolean.FALSE);
            map.put("error", e.getMessage());
        }

        return map;
    }

    // 인증번호 일치여부 확인
    @GetMapping("/check")
    public String mailCheck(@RequestParam String token) {

        boolean isMatch = emailVerificationService.checkMail(token);
        if(isMatch){
            return "successPage";
        }

        return "failPage";
    }
}
