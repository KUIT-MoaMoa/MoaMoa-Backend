package com.kuit.moamoa.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class TestController {
    @GetMapping("/test")
    public void testLogs() {
        log.info("logs created");
    }
}
