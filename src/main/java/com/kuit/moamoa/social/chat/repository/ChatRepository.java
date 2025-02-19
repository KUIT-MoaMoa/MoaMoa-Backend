package com.kuit.moamoa.social.chat.repository;

import com.kuit.moamoa.social.chat.domain.Chat;
import com.kuit.moamoa.global.Status;
import com.kuit.moamoa.user.domain.User;
import com.kuit.moamoa.social.usergroup.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

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


    @Query("SELECT COUNT(c) FROM Chat c " +
            "WHERE c.userGroup = :userGroup " +
            "AND c.status = 'ACTIVE' " +
            "AND NOT EXISTS (" +
            "    SELECT 1 FROM ChatReadStatus rs " +
            "    WHERE rs.chat = c " +
            "    AND rs.user = :user" +
            ")")
    long countUnreadMessages(@Param("userGroup") UserGroup userGroup, @Param("user") User user);
}
