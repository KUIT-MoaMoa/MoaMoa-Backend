package com.kuit.moamoa.global.notification.dto;

import com.kuit.moamoa.global.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String content;
    private NotificationType type;
    private Long relationId;
    private LocalDateTime createdAt;
}
