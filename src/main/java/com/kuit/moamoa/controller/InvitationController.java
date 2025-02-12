package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.InvitationUrlResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.InvitationService;
import com.kuit.moamoa.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invitation")
@Slf4j
public class InvitationController {

    @GetMapping("")
    public String invitation(@RequestParam String nickname, HttpServletResponse response) {
        Cookie cookie = new Cookie("invitation_nickname", nickname);

        cookie.setPath("https://moa-moa-frontend-individual.vercel.app");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600);
         cookie.setSecure(true);

        response.addCookie(cookie);
        return "redirect:https://moa-moa-frontend-individual.vercel.app";
    }
}
