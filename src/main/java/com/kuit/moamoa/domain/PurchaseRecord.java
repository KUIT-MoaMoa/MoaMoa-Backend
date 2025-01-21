package com.kuit.moamoa.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_records")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PurchaseRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name; // item 이름

    @Column(nullable = false)
    private Long transaction;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void setUser(User user) {
        this.user = user;
        if (!user.getPurchaseRecords().contains(this)) {
            user.getPurchaseRecords().add(this);
        }
    }

}
