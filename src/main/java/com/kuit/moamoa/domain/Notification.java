package com.kuit.moamoa.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "notifications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 알림을 받는 사용자

    @Column(nullable = false)
    private String content; // 알림 내용

    @Column(nullable = false)
    private NotificationType type; // 알림 유형 (FRIEND_REQUEST 등)

    @Column(nullable = false)
    private Long relationId; // 관련 데이터 ID (친구 요청 ID, 챌린지 ID 등)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Notification(User user, String content, NotificationType type, Long relationId, Status status) {
        this.user = user;
        this.content = content;
        this.type = type;
        this.relationId = relationId;
        this.status = status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
