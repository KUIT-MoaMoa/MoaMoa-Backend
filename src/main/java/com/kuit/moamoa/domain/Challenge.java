package com.kuit.moamoa.domain;

import com.kuit.moamoa.global.exception.ErrorCode;
import com.kuit.moamoa.global.exception.GlobalException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private long  duration;

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

    //챌린지 시작일
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    //챌린지 종료일
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    //챌린지 모집 마감일
    @Column(name = "recruitment_deadline", nullable = false)
    private LocalDateTime recruitmentDeadline;

    //challenge를 진행 중인 유저들의 사용 퍼센트
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeProgress> progressList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_id")
    private UserGroup userGroup;  // 그룹과 연관될 수도, 아닐 수도 있음 (null 허용)

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "challenge_status", nullable = false)
    private ChallengeStatus status;

    @Builder
    public Challenge(String title, String content, Integer headCount,  Boolean publicChallenge,
                     Integer goalAmount, Integer battleCoin, ChallengeCategory challengeCategory,
                     LocalDateTime startDate, LocalDateTime endDate, UserGroup userGroup)
    {
        validateChallenge(title, headCount, goalAmount, battleCoin, startDate, endDate);

        this.title = title;
        this.content = content;
        this.headCount = headCount;
        this.duration = ChronoUnit.DAYS.between(startDate, endDate);
        this.publicChallenge = publicChallenge;
        this.goalAmount = goalAmount;
        this.battleCoin = battleCoin;
        this.challengeCategory = challengeCategory;
        this.startDate = startDate;
        this.endDate = endDate;
        this.recruitmentDeadline = startDate.minusDays(1).withHour(23).withMinute(59).withSecond(59);
        this.status = ChallengeStatus.RECRUITING;
        this.userGroup = userGroup;
    }

    public String getTitle() {
        return title;
    }

    public Long getDuration() {
        return duration;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public List<ChallengeProgress> getProgressList() {
        return progressList;
    }

    private void validateChallenge(String title, Integer headCount, Integer goalAmount, Integer battleCoin,
                                   LocalDateTime startDate, LocalDateTime endDate) {
        if (title == null || title.trim().isEmpty()) {
            throw new GlobalException(ErrorCode.INVALID_INPUT, "Title cannot be empty");
        }

        if (headCount <= 1) {
            throw new GlobalException(ErrorCode.INVALID_INPUT, "Head count must be greater than 1");
        }

        if (goalAmount <= 0) {
            throw new GlobalException(ErrorCode.INVALID_AMOUNT, "Goal amount must be greater than 0");
        }

        if (battleCoin <= 0) {
            throw new GlobalException(ErrorCode.INVALID_AMOUNT, "Battle coin must be greater than 0");
        }

        if (startDate.isBefore(LocalDateTime.now())) {
            throw new GlobalException(ErrorCode.INVALID_DATE, "Start date cannot be in the past");
        }

        if (endDate.isBefore(startDate)) {
            throw new GlobalException(ErrorCode.INVALID_DATE, "End date must be after start date");
        }
    }


    public void startChallenge() {
        if (this.status != ChallengeStatus.RECRUITING) {
            throw new GlobalException(ErrorCode.INVALID_STATUS, "Cannot start challenge from " + this.status);
        }

        this.status = ChallengeStatus.ONGOING;
    }


    public void completeChallenge() {
        if (this.status != ChallengeStatus.ONGOING) {
            throw new GlobalException(ErrorCode.INVALID_STATUS, "Cannot complete challenge from " + this.status);
        }

        if (LocalDateTime.now().isBefore(this.endDate)) {
            throw new GlobalException(ErrorCode.INVALID_DATE, "Cannot complete challenge before end date");
        }

        this.status = ChallengeStatus.COMPLETED;
    }

    public void addParticipant(User user) {
        if (this.status != ChallengeStatus.RECRUITING) {
            throw new GlobalException(ErrorCode.INVALID_STATUS, "Challenge is not in recruiting status");
        }

        if (this.progressList.size() >= this.headCount) {
            throw new GlobalException(ErrorCode.CHALLENGE_FULL, "Challenge has reached maximum participants");
        }

        boolean isAlreadyParticipating = this.progressList.stream()
                .anyMatch(progress -> progress.getUser().getId().equals(user.getId()));
        if (isAlreadyParticipating) {
            throw new GlobalException(ErrorCode.ALREADY_PARTICIPATING, "User is already participating in this challenge");
        }

        if (LocalDateTime.now().isAfter(this.recruitmentDeadline)) {
            throw new GlobalException(ErrorCode.RECRUITMENT_CLOSED, "Recruitment period has ended");
        }

        ChallengeProgress progress = new ChallengeProgress(this, user, Status.ACTIVE);
        this.progressList.add(progress);
    }

    public void removeParticipant(User user) {
        ChallengeProgress progress = this.progressList.stream()
                .filter(p -> p.getUser().equals(user))
                .findFirst()
                .orElseThrow(() -> new GlobalException(ErrorCode.NOT_PARTICIPATING, "User is not participating"));

        this.progressList.remove(progress);
    }

    public void cancel() {
        if (this.status == ChallengeStatus.COMPLETED) {
            throw new GlobalException(ErrorCode.INVALID_STATUS, "Cannot cancel completed challenge");
        }
        this.status = ChallengeStatus.CANCELED;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }
}