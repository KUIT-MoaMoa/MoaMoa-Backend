package com.kuit.moamoa.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // b/c reflection api
@AllArgsConstructor
@Builder
@Getter
@Table(name = "consumptions")
public class Consumption{
    @Id
    @Column(name = "consumption_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ConsumptionCategory consumptionCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumption_challenge_id")
    private ConsumptionChallenge consumptionChallenge;

    @Enumerated(EnumType.STRING)
    private ChallengeCategory challengeCategory;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    // 양방향 관계: 편의 메서드
    public void setUser(User user) {
        this.user = user;
        if (!user.getConsumptions().contains(this)) {
            user.getConsumptions().add(this);
        }
    }

    public void setConsumptionChallenge(ConsumptionChallenge consumptionChallenge) {
        this.consumptionChallenge = consumptionChallenge;
        if (!consumptionChallenge.getConsumptions().contains(this)) {
            consumptionChallenge.getConsumptions().add(this);
        }
    }
}
