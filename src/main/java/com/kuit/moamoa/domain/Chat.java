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
@Table(name = "chats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Builder
    public Chat(String content, Status status) {
        this.content = content;
        this.status = status;
    }

    // 양방향 관계: 편의 메서드
    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
        if (userGroup.getChat() != this) {
            userGroup.setChat(this);
        }
    }

    // 양방향 관계: 편의 메서드
    public void setUser(User user) {
        this.user = user;
        if (!user.getChats().contains(this)) {
            user.getChats().add(this);
        }
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        this.status = Status.INACTIVE;
    }
}