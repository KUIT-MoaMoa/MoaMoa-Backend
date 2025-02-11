package com.kuit.moamoa.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "user_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//채팅방
public class UserGroup {
    @Id
    @Column(name = "user_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "userGroup")
    private List<Challenge> challenges = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "userGroup")
    private List<UserUserGroupJunction> userUserGroupJunctions = new ArrayList<>();

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL)
    private List<Chat> chats = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    // 현재 진행 중인 챌린지를 가져오는 메서드
    public Optional<Challenge> getCurrentChallenge() {
        return challenges.stream()
                .filter(challenge -> challenge.getStatus() == ChallengeStatus.ONGOING)
                .findFirst();
    }

    // 양방향 관계: 편의 메서드
    public void addChat(Chat chat) {
        this.chats.add(chat);
        if (chat.getUserGroup() != this) {
            chat.setUserGroup(this);
        }
    }

    // 양방향 관계: 편의 메서드
    public void addUserUserGroupJunction(UserUserGroupJunction junction) {
        this.userUserGroupJunctions.add(junction);
        if (junction.getUserGroup() != this) {
            junction.setUserGroup(this);
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UserGroup(String title) {
        this.title = title;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
