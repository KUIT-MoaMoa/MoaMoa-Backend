package com.kuit.moamoa.controller;

import com.kuit.moamoa.dto.response.notification.NotificationResponse;
import com.kuit.moamoa.global.response.ApiResponse;
import com.kuit.moamoa.jwt.Jwt;
import com.kuit.moamoa.service.NotificationService;
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
