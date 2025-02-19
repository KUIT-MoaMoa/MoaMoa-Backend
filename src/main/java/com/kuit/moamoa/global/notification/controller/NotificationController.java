package com.kuit.moamoa.global.notification.controller;

import com.kuit.moamoa.global.notification.dto.NotificationResponse;
import com.kuit.moamoa.configuration.response.ApiResponse;
import com.kuit.moamoa.global.jwt.Jwt;
import com.kuit.moamoa.global.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<List<NotificationResponse>> getNotifications(@Jwt Long userId) {

        List<NotificationResponse> notifications = notificationService.getNotifications(userId);
        return new ApiResponse<>(notifications);
    }
}
