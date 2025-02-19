package com.kuit.moamoa.social.chat.domain;

import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.social.usergroup.domain.UserGroup;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatReadStatus> readStatuses = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
        if (!userGroup.getChats().contains(this)) {
            userGroup.addChat(this);
        }
    }

    public void setUser(User user) {
        this.user = user;
        if (!user.getChats().contains(this)) {
            user.getChats().add(this);
        }
    }

    public void markAsReadBy(User user) {
        if (!isReadBy(user)) {
            ChatReadStatus readStatus = ChatReadStatus.builder()
                    .chat(this)
                    .user(user)
                    .build();
            this.readStatuses.add(readStatus);
        }
    }

    public boolean isReadBy(User user) {
        return this.readStatuses.stream()
                .anyMatch(status -> status.getUser().equals(user));
    }

    @Builder
    public Chat(String content, Status status) {
        this.content = content;
        this.status = status;
    }
}