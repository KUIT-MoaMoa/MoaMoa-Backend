package com.kuit.moamoa.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String login_id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column
    @Lob
    private String profile_image; // 확인 필요: db에서 이미지를 어떤 방식으로 저장하는지

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int coin;

    @OneToMany(mappedBy = "user")
    private List<PurchaseRecord> purchaseRecords = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Attendance> attendances = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;
  
    @OneToMany(mappedBy = "user")
    private List<Consumption> consumptions;

    @OneToMany(mappedBy = "user")
    private List<ChallengeRecord> challengeRecords;

    public void addAttendances(Attendance attendance){
        attendances.add(attendance);
        attendance.setUser(this);
    }

    public void addPurchaseRecords(PurchaseRecord purchaseRecord){
        purchaseRecords.add(purchaseRecord);
        purchaseRecord.setUser(this);
    }

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
