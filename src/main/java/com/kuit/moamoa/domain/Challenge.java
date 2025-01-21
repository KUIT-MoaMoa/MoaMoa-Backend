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
@Table(name = "challenges")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Challenge {
    @Id
    @Column(name = "challenge_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "head_count", nullable = false)
    private Integer headCount;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "battle_coin", nullable = false)
    private Integer battleCoin;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_category", nullable = false)
    private ChallengeCategory challengeCategory;

    @OneToMany(mappedBy = "challenge")
    private List<UserGroup> userGroups = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    // 양방향 관계: 편의 메서드
    public void addUserGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
        if (userGroup.getChallenge() != this) {
            userGroup.setChallenge(this);
        }
    }
}