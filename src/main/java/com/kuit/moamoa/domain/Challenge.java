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

    @Column(name = "public_challenge", nullable = false)
    private Boolean publicChallenge;

    //목표 금액이 필요할듯
    @Column(name = "goal_amount", nullable = false)
    private Integer goalAmount;

    @Column(name = "battle_coin", nullable = false)
    private Integer battleCoin;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_category", nullable = false)
    private ChallengeCategory challengeCategory;

    //challenge를 진행 중인 유저들의 사용 퍼센트
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeProgress> progressList = new ArrayList<>();

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

    public Challenge(String title, String content, Integer headCount, Integer duration, Boolean publicChallenge, Integer goalAmount, Integer battleCoin, ChallengeCategory challengeCategory) {
        this.title = title;
        this.content = content;
        this.headCount = headCount;
        this.duration = duration;
        this.publicChallenge = publicChallenge;
        this.goalAmount = goalAmount;
        this.battleCoin = battleCoin;
        this.challengeCategory = challengeCategory;
        this.status = Status.ACTIVE; // 기본적으로 활성 상태로 설정
    }
}