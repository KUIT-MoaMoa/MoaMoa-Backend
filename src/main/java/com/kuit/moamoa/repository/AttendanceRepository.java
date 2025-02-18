package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Attendance;
import com.kuit.moamoa.domain.User;
import lombok.Builder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findTopByUserIdOrderByCreatedAtDesc(Long userId);
    List<Attendance> findAllByUserId(Long userId);

    // 2주 이상 미접속 여부 확인
    @Query("SELECT COUNT(a) = 0 FROM Attendance a WHERE a.user = :user AND a.createdAt >= :twoWeeksAgo")
    boolean hasNotAttendedInLastTwoWeeks(@Param("user") User user, @Param("twoWeeksAgo") LocalDateTime twoWeeksAgo);

}
