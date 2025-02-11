package com.kuit.moamoa.controller;

import com.kuit.moamoa.jwt.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class TestController {
    @GetMapping("/test")
    public String testLogs(@Jwt Long userId) {
        log.info("logs created");
        return userId.toString();
    }
}
