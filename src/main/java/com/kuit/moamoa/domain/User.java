package com.kuit.moamoa.domain;

import jakarta.persistence.*;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Consumption> consumptions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ChallengeRecord> challengeRecords = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserUserGroupJunction> userUserGroupJunctions = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Chat> chats = new ArrayList<>();

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
}