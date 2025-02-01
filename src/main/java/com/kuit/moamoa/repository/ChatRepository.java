package com.kuit.moamoa.repository;

import com.kuit.moamoa.domain.Chat;
import com.kuit.moamoa.domain.Status;
import com.kuit.moamoa.domain.User;
import com.kuit.moamoa.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

// 7. Repository 완성본
@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    // UserGroup별 채팅 내역 조회 (최신순)
    List<Chat> findByUserGroupAndStatusOrderByCreatedAtDesc(UserGroup userGroup, Status status);

    // UserGroup별 특정 시간 이후의 채팅 내역 조회
    @Query("SELECT c FROM Chat c WHERE c.userGroup = :userGroup AND c.status = :status " +
            "AND c.createdAt > :since ORDER BY c.createdAt DESC")
    List<Chat> findRecentMessages(@Param("userGroup") UserGroup userGroup,
                                  @Param("status") Status status,
                                  @Param("since") LocalDateTime since);

    // 특정 사용자의 채팅 내역 조회
    List<Chat> findByUserAndStatusOrderByCreatedAtDesc(User user, Status status);

    // UserGroup과 User로 채팅 내역 조회
    List<Chat> findByUserGroupAndUserAndStatusOrderByCreatedAtDesc(
            UserGroup userGroup, User user, Status status);

    // 특정 기간 동안의 채팅 내역 조회
    @Query("SELECT c FROM Chat c WHERE c.userGroup = :userGroup " +
            "AND c.createdAt BETWEEN :startDate AND :endDate " +
            "AND c.status = :status ORDER BY c.createdAt DESC")
    List<Chat> findByDateRange(@Param("userGroup") UserGroup userGroup,
                               @Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate,
                               @Param("status") Status status);
}
