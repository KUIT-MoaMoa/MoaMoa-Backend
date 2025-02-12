package com.kuit.moamoa.domain;

import com.kuit.moamoa.global.exception.ErrorCode;
import com.kuit.moamoa.global.exception.GlobalException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Builder
    public User(String nickname, String password, String role, String email) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

//    @Builder
//    public User(String nickname, String role) {
//        this.nickname = nickname;
//        this.role = role;
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String role;

    @Column(nullable = false)
    private String email;

    @Column //소셜로그인 유저는 null
    @Setter
    private String password;

    @Setter
    private String nickname;

    @Column
    private String imageUrl;

    @Column(nullable = false)
    private Integer coin=0;

    @OneToMany(mappedBy = "user")
    private List<PurchaseRecord> purchaseRecords = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Attendance> attendances = new ArrayList<>();
  
    @OneToMany(mappedBy = "user")
    private List<Consumption> consumptions;

    @OneToMany(mappedBy = "user")
    private List<ChallengeRecord> challengeRecords;

    @OneToMany(mappedBy = "user")
    private List<UserUserGroupJunction> userUserGroupJunctions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Chat> chats = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

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

    // 양방향 관계: 편의 메서드
    public void addUserUserGroupJunction(UserUserGroupJunction junction) {
        this.userUserGroupJunctions.add(junction);
        if (junction.getUser() != this) {
            junction.setUser(this);
        }
    }

    // 양방향 관계: 편의 메서드
    public void addChat(Chat chat) {
        this.chats.add(chat);
        if (chat.getUser() != this) {
            chat.setUser(this);
        }
    }

    public void deductBattleCoins(int amount) {
        if (this.coin < amount) {
            throw new GlobalException(ErrorCode.INSUFFICIENT_BATTLE_COINS, "Insufficient battle coins!");
        }
        this.coin -= amount;
    }

    public void addBattleCoins(int amount) {
        this.coin += amount;
    }

    public int getBattleCoins() {
        return this.coin;
    }
}
