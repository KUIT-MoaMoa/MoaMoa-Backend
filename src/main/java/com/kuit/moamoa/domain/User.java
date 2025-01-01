package com.kuit.moamoa.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Consumption> consumptions;

    @OneToMany(mappedBy = "user")
    private List<ChallengeRecord> challengeRecords;

    // 양방향 관계: 편의 메서드
    public void addConsumption(Consumption consumption) {
        this.consumptions.add(consumption);
        if (consumption.getUser() != this) {
            consumption.setUser(this);
        }
    }

    // 양방향 관계: 편의 메서드
    public void addChallengeRecords(ChallengeRecord challengeRecord) {
        this.challengeRecords.add(challengeRecord);
        if (challengeRecord.getUser() != this) {
            challengeRecord.setUser(this);
        }
    }
}
