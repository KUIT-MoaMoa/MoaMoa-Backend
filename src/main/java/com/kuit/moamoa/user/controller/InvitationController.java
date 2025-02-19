package com.kuit.moamoa.user.controller;

import com.kuit.moamoa.global.jwt.Jwt;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invitation")
@Slf4j
public class InvitationController {
    private final UserRepository userRepository;

    @GetMapping("")
    public String invitation(@RequestParam String nickname, HttpServletResponse response) {
        Cookie cookie = new Cookie("invitation_nickname", nickname);    // base64 encoding 상태

        log.warn("{}", nickname);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
         cookie.setSecure(true);

        response.addCookie(cookie);
        return "redirect:https://moa-moa-frontend-individual.vercel.app";
    }
}
