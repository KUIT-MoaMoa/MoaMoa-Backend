package com.kuit.moamoa.controller;

import com.kuit.moamoa.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/verify-email")
@Slf4j
@RequiredArgsConstructor
@Tag(name="이메일 인증", description = "소셜 로그인 연동이 안될 경우 이메일 인증을 진행하는 경로입니다.")
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;
    private int number;

    @PostMapping("/send")
    public HashMap<String, Object> mailSend(String mail) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            number = emailVerificationService.sendMail(mail);
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
    public ResponseEntity<?> mailCheck(@RequestParam String token) {

//        boolean isMatch = userNumber.equals(String.valueOf(number));
        boolean isMatch = emailVerificationService.checkMail(token);

        return ResponseEntity.ok(isMatch);
    }
}
