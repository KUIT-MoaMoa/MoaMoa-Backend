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

@Entity
@Table(name = "user_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroup {
    @Id
    @Column(name = "user_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "userGroup")
    private List<UserUserGroupJunction> userUserGroupJunctions = new ArrayList<>();

    @OneToOne(mappedBy = "userGroup", cascade = CascadeType.ALL)
    private Chat chat;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    // 양방향 관계: 편의 메서드
    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
        if (!challenge.getUserGroups().contains(this)) {
            challenge.getUserGroups().add(this);
        }
    }

    // 양방향 관계: 편의 메서드
    public void setChat(Chat chat) {
        this.chat = chat;
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
}
