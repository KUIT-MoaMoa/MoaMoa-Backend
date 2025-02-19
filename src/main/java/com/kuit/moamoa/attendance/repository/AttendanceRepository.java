package com.kuit.moamoa.attendance.repository;

import com.kuit.moamoa.user.domain.Attendance;
import com.kuit.moamoa.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findTopByUserIdOrderByCreatedAtDesc(Long userId);
    List<Attendance> findAllByUserId(Long userId);

    //2주 이상 미접속 여부 확인
    @Query("SELECT CASE WHEN COUNT(a) = 0 THEN true WHEN MAX(a.createdAt) < :nowMinusTwoWeeks THEN false ELSE true END FROM Attendance a WHERE a.user = :user")
    boolean hasAttendedRecently(@Param("user") User user, @Param("nowMinusTwoWeeks") LocalDateTime nowMinusTwoWeeks);

    //이번주 출석 요일
    @Query("SELECT DISTINCT DATE(a.createdAt) FROM Attendance a WHERE a.user = :user AND a.createdAt >= :weekStart AND a.createdAt <= :weekEnd")
    List<LocalDate> findThisWeekUniqueAttendanceDates(@Param("user") User user, @Param("weekStart") LocalDate weekStart, @Param("weekEnd") LocalDate weekEnd);

}
