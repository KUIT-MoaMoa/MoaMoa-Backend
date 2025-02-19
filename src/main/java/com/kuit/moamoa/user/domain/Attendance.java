package com.kuit.moamoa.user.domain;

import com.kuit.moamoa.global.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void setUser(User user) {
        this.user = user;
        if (!user.getAttendances().contains(this)) {
            user.getAttendances().add(this);
        }
    }

    @Builder
    public Attendance(User user, Status status) {
        this.user = user;
        this.status = status;
    }

}
