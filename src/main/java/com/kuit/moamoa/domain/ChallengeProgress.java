package com.kuit.moamoa.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "challenge_progress")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChallengeProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_progress_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "used_amount", nullable = false)
    private Integer usedAmount = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public ChallengeProgress(Challenge challenge, User user) {
        this.challenge = challenge;
        this.user = user;
    }

    //해당 첼린지에서 유저가 사용한 금액
    public void updateUsedAmount(int amount) {
        this.usedAmount += amount;
    }

    public double getUsagePercentage() {
        return (challenge.getGoalAmount() == 0) ? 0 : ((double) usedAmount / challenge.getGoalAmount()) * 100;
    }
}