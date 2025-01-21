package com.kuit.moamoa.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item {
    // 화면설계서에서 기준 구매 가능 아이템이 '프로필 테두리'밖에 보이지 않아서 id, name만 만들었습니다

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long transaction; // 가격

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

}
