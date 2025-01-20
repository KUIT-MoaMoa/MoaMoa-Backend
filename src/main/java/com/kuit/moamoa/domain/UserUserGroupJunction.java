package com.kuit.moamoa.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_user_group_junction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserUserGroupJunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    // 연관관계 편의 메서드
    public void setUser(User user) {
        this.user = user;
        if (!user.getUserUserGroupJunctions().contains(this)) {
            user.getUserUserGroupJunctions().add(this);
        }
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
        if (!userGroup.getUserUserGroupJunctions().contains(this)) {
            userGroup.getUserUserGroupJunctions().add(this);
        }
    }
}